package com.fleettools.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerRespawnMixin {

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void fleettools_onPlayerRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        // No longer needed - Minecraft's native keep inventory handles everything automatically
        // when we temporarily enable the gamerule during death
    }
}