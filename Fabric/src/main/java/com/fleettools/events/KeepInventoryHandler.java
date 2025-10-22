package com.fleettools.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import com.fleettools.data.PlayerDataManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeepInventoryHandler {
    
    // Store player inventories temporarily when they die with keep inventory enabled
    private static final Map<UUID, PlayerInventoryData> storedInventories = new HashMap<>();
    
    private static class PlayerInventoryData {
        public final DefaultedList<ItemStack> main;
        public final DefaultedList<ItemStack> armor;
        public final DefaultedList<ItemStack> offhand;
        public final int selectedSlot;
        public final long deathTime;
        
        public PlayerInventoryData(ServerPlayerEntity player) {
            this.main = DefaultedList.ofSize(36, ItemStack.EMPTY);
            this.armor = DefaultedList.ofSize(4, ItemStack.EMPTY);
            this.offhand = DefaultedList.ofSize(1, ItemStack.EMPTY);
            this.selectedSlot = player.getInventory().selectedSlot;
            this.deathTime = System.currentTimeMillis();
            
            // Copy inventory contents
            for (int i = 0; i < 36; i++) {
                this.main.set(i, player.getInventory().main.get(i).copy());
            }
            for (int i = 0; i < 4; i++) {
                this.armor.set(i, player.getInventory().armor.get(i).copy());
            }
            this.offhand.set(0, player.getInventory().offHand.get(0).copy());
        }
        
        public void restore(ServerPlayerEntity player) {
            // Clear current inventory
            player.getInventory().clear();
            
            // Restore saved inventory
            for (int i = 0; i < 36; i++) {
                player.getInventory().main.set(i, this.main.get(i).copy());
            }
            for (int i = 0; i < 4; i++) {
                player.getInventory().armor.set(i, this.armor.get(i).copy());
            }
            player.getInventory().offHand.set(0, this.offhand.get(0).copy());
            player.getInventory().selectedSlot = this.selectedSlot;
            
            // Mark inventory as changed
            player.currentScreenHandler.sendContentUpdates();
            player.playerScreenHandler.onContentChanged(player.getInventory());
            player.sendAbilitiesUpdate();
        }
    }
    
    public static void register() {
        // Register for entity death events - capture inventory before death
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof ServerPlayerEntity player) {
                boolean keepInv = PlayerDataManager.getKeepInventory(player);
                if (keepInv) {
                    // Store the player's inventory before death
                    storedInventories.put(player.getUuid(), new PlayerInventoryData(player));
                    
                    // Immediately set keep inventory gamerule to true temporarily
                    boolean wasEnabled = player.getWorld().getGameRules().getBoolean(net.minecraft.world.GameRules.KEEP_INVENTORY);
                    if (!wasEnabled) {
                        player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY).set(true, player.getServer());
                        
                        // Schedule to restore the gamerule after a short delay
                        player.getServer().execute(() -> {
                            try {
                                Thread.sleep(50);
                                player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY).set(false, player.getServer());
                            } catch (Exception e) {
                                // Ignore errors
                            }
                        });
                    }
                }
            }
            return true; // Always allow death to proceed
        });
        
        // Handle respawn to ensure inventory is maintained
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (!alive) {
                UUID playerUuid = newPlayer.getUuid();
                PlayerInventoryData storedInv = storedInventories.get(playerUuid);
                
                if (storedInv != null) {
                    // Restore inventory after respawn
                    storedInv.restore(newPlayer);
                    
                    // Clean up stored data
                    storedInventories.remove(playerUuid);
                    
                    // Send confirmation message
                    newPlayer.sendMessage(net.minecraft.text.Text.literal("§aYour inventory has been restored! §7(XP was still lost)"), false);
                }
            }
        });
        
        // Server tick event to clean up old stored inventories (in case something goes wrong)
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            long currentTime = System.currentTimeMillis();
            storedInventories.entrySet().removeIf(entry -> 
                currentTime - entry.getValue().deathTime > 30000 // Remove after 30 seconds
            );
        });
    }
}