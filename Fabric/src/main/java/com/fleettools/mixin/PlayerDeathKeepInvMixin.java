package com.fleettools.mixin;

import com.fleettools.data.PlayerDataManager;
import com.fleettools.data.KeepInventoryStorage;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathKeepInvMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void fleettools_onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Check if player has keep inventory enabled
        if (PlayerDataManager.getKeepInventory(player)) {
            // Store the current inventory and equipment before vanilla death logic
            PlayerInventory inventory = player.getInventory();

            // Store all items from main inventory, armor, and offhand
            DefaultedList<ItemStack> mainInventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
            DefaultedList<ItemStack> armorInventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
            DefaultedList<ItemStack> offHandInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

            // Copy all items
            for (int i = 0; i < inventory.main.size(); i++) {
                mainInventory.set(i, inventory.main.get(i).copy());
            }
            for (int i = 0; i < inventory.armor.size(); i++) {
                armorInventory.set(i, inventory.armor.get(i).copy());
            }
            for (int i = 0; i < inventory.offHand.size(); i++) {
                offHandInventory.set(i, inventory.offHand.get(i).copy());
            }

            // Store the inventories for restoration after respawn
            KeepInventoryStorage.storePlayerInventory(player.getUuid(), mainInventory, armorInventory,
                    offHandInventory);

            // Clear the inventory so items don't drop
            inventory.clear();
        }
    }
}