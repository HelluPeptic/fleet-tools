package com.fleettools.events;

import com.fleettools.data.PlayerDataManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class KeepInventoryHandler {

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity player) {
                // Check if this player has keep inventory enabled
                if (PlayerDataManager.getKeepInventory(player)) {
                    // Reset experience to 0 (lose XP even with keep inventory)
                    player.setExperiencePoints(0);
                    player.setExperienceLevel(0);
                }
            }
        });
    }
}