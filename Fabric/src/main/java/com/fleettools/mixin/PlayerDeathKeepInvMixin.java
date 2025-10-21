package com.fleettools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fleettools.data.PlayerDataManager;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathKeepInvMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void fleettools_beforePlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
        // Check if player has keep inventory enabled
        boolean keepInvEnabled = PlayerDataManager.getKeepInventory(player);
        
        if (keepInvEnabled) {
            // Ensure keep inventory is enabled before death processing
            player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(true, player.getServer());
            System.out.println("[FleetTools] Enabled keep inventory before death for: " + player.getName().getString());
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void fleettools_afterPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
        // Always disable keep inventory after death to ensure other players drop items normally
        player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).set(false, player.getServer());
        System.out.println("[FleetTools] Restored keep inventory to false after death");
    }
}