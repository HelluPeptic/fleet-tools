package com.fleettools.events;

import com.fleettools.data.PlayerDataManager;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class KeepInventoryHandler {
    
    public static void register() {
        // Register for entity death events - capture inventory before death
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (entity instanceof ServerPlayerEntity player) {
                boolean keepInv = PlayerDataManager.getKeepInventory(player);
                if (keepInv) {
                    // Store the player's inventory in persistent storage
                    PlayerDataManager.storeInventoryOnDeath(player);
                    
                    // Temporarily enable keep inventory gamerule for this death
                    boolean wasEnabled = player.getWorld().getGameRules().getBoolean(net.minecraft.world.GameRules.KEEP_INVENTORY);
                    if (!wasEnabled) {
                        player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY).set(true, player.getServer());
                        
                        // Schedule to restore the gamerule after a short delay
                        if (player.getServer() != null) {
                            player.getServer().execute(() -> {
                                try {
                                    Thread.sleep(50);
                                    player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY).set(false, player.getServer());
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            });
                        }
                    }
                }
            }
            return true; // Always allow death to proceed
        });
        
        // Handle respawn to ensure inventory is maintained
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (!alive && PlayerDataManager.hasStoredInventory(newPlayer)) {
                // Restore inventory after respawn
                PlayerDataManager.restoreInventoryOnRespawn(newPlayer);
                
                // Send confirmation message
                newPlayer.sendMessage(net.minecraft.text.Text.literal("§aYour inventory has been restored! §7(XP was still lost)"), false);
            }
        });
        
        // Handle player joining to restore inventory if they had one stored when they disconnected
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (PlayerDataManager.hasStoredInventory(player)) {
                // Player joined while having a stored inventory (probably disconnected while dead)
                PlayerDataManager.restoreInventoryOnRespawn(player);
                
                // Send confirmation message
                player.sendMessage(net.minecraft.text.Text.literal("§aYour inventory has been restored from before you died! §7(XP was still lost)"), false);
            }
        });
    }
}