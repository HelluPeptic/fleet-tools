package com.fleettools.mixin;

import com.fleettools.data.PlayerDataManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityKeepInvMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeathHead(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Check if this player has keep inventory enabled
        if (PlayerDataManager.getKeepInventory(player)) {
            // Store the current keepInventoryOnDeath gamerule value
            boolean originalKeepInventory = player.getWorld().getGameRules()
                    .getBoolean(net.minecraft.world.GameRules.KEEP_INVENTORY);

            // Temporarily enable keep inventory for this player's death
            player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY).set(true,
                    player.getServer());

            // Schedule restoration of the original gamerule after death processing
            player.getServer().execute(() -> {
                player.getWorld().getGameRules().get(net.minecraft.world.GameRules.KEEP_INVENTORY)
                        .set(originalKeepInventory, player.getServer());
            });
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onPlayerDeathTail(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Check if this player has keep inventory enabled
        if (PlayerDataManager.getKeepInventory(player)) {
            // Reset experience to 0 (lose XP even with keep inventory)
            player.setExperiencePoints(0);
            player.setExperienceLevel(0);
        }
    }
}