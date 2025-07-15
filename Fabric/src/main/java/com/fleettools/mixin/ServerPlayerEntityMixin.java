package com.fleettools.mixin;

import com.fleettools.data.PlayerDataManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerEntity.class, priority = 500)
public class ServerPlayerEntityMixin {

    // Capture vanilla /tp and coordinate teleports for /back (correct signature for 1.20.1)
    @Inject(method = "teleport", at = @At("HEAD"))
    private void fleettools_onTeleport(net.minecraft.server.world.ServerWorld destination, double x, double y, double z, java.util.Set<?> movementFlags, float yaw, float pitch, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerDataManager.setLastLocation(player, player.getPos(), player.getServerWorld());
    }

    // Capture world change teleports (e.g., /tp <player> <dim>)
    @Inject(method = "moveToWorld", at = @At("HEAD"))
    private void fleettools_onMoveToWorld(net.minecraft.server.world.ServerWorld destination, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerDataManager.setLastLocation(player, player.getPos(), player.getServerWorld());
    }
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void fleettools_onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        
        // Check if player has god mode enabled
        if (PlayerDataManager.getGodMode(player)) {
            // Cancel damage if player is in god mode
            cir.setReturnValue(false);
        }
    }
}
