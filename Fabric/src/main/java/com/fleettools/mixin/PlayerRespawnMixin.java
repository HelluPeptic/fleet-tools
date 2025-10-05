package com.fleettools.mixin;

import com.fleettools.data.PlayerDataManager;
import com.fleettools.data.KeepInventoryStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerRespawnMixin {

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void fleettools_onPlayerRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Check if we have stored inventory for this player and they have keep
        // inventory enabled
        if (KeepInventoryStorage.hasStoredInventory(player.getUuid()) && PlayerDataManager.getKeepInventory(player)) {
            KeepInventoryStorage.StoredInventory stored = KeepInventoryStorage.removeStoredInventory(player.getUuid());

            if (stored != null) {
                PlayerInventory inventory = player.getInventory();

                // Restore main inventory
                for (int i = 0; i < stored.mainInventory.size() && i < inventory.main.size(); i++) {
                    inventory.main.set(i, stored.mainInventory.get(i));
                }

                // Restore armor
                for (int i = 0; i < stored.armorInventory.size() && i < inventory.armor.size(); i++) {
                    inventory.armor.set(i, stored.armorInventory.get(i));
                }

                // Restore offhand
                for (int i = 0; i < stored.offHandInventory.size() && i < inventory.offHand.size(); i++) {
                    inventory.offHand.set(i, stored.offHandInventory.get(i));
                }

                // Mark inventory as changed
                inventory.markDirty();
            }
        }
    }
}