package com.fleettools.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import com.fleettools.data.PlayerDataManager;

public class PlayerJoinHandler {
    
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            
            // Restore fly abilities
            boolean flyEnabled = PlayerDataManager.getFlyEnabled(player);
            if (flyEnabled) {
                player.getAbilities().allowFlying = true;
                player.sendAbilitiesUpdate();
            }
            
            // Restore god mode
            boolean godMode = PlayerDataManager.getGodMode(player);
            if (godMode) {
                player.getAbilities().invulnerable = true;
                player.sendAbilitiesUpdate();
            }
        });
    }
}
