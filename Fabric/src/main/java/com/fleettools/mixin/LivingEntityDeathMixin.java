package com.fleettools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fleettools.data.PlayerDataManager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;

@Mixin(LivingEntity.class)
public class LivingEntityDeathMixin {

    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void fleettools_preventPlayerInventoryDrop(CallbackInfo ci) {
        // Only handle ServerPlayerEntity
        if ((Object) this instanceof ServerPlayerEntity player) {
            boolean keepInvEnabled = PlayerDataManager.getKeepInventory(player);
            
            if (keepInvEnabled) {
                // Cancel inventory dropping completely for keep inventory players
                System.out.println("[FleetTools] Prevented inventory drop for keep inventory player: " + player.getName().getString());
                ci.cancel();
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void fleettools_beforePlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        // Only handle ServerPlayerEntity
        if ((Object) this instanceof ServerPlayerEntity player) {
            boolean keepInvEnabled = PlayerDataManager.getKeepInventory(player);
            
            if (keepInvEnabled) {
                // Enable keep inventory before death processing
                player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(true, player.getServer());
                System.out.println("[FleetTools] Enabled keep inventory before death for: " + player.getName().getString());
            }
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void fleettools_afterPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        // Only handle ServerPlayerEntity
        if ((Object) this instanceof ServerPlayerEntity player) {
            // Always disable keep inventory after death to ensure other players drop items normally
            player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(false, player.getServer());
            System.out.println("[FleetTools] Restored keep inventory to false after death");
        }
    }
}