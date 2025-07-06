package com.fleettools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.fleettools.commands.*;
import com.fleettools.data.PlayerDataManager;
import com.fleettools.events.PlayerJoinHandler;

public class FleettoolsMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("[FLEET TOOLS] Initializing Fleet Tools mod");
        
        // Initialize data manager when server starts
        ServerLifecycleEvents.SERVER_STARTING.register(PlayerDataManager::init);
        
        // Register player join handler
        PlayerJoinHandler.register();
        
        // Register all commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HomeCommand.register(dispatcher, registryAccess, environment);
            SpawnCommand.register(dispatcher, registryAccess, environment);
            BackCommand.register(dispatcher, registryAccess, environment);
            HealCommand.register(dispatcher, registryAccess, environment);
            FeedCommand.register(dispatcher, registryAccess, environment);
            FlyCommand.register(dispatcher, registryAccess, environment);
            GamemodeCommand.register(dispatcher, registryAccess, environment);
            GodCommand.register(dispatcher, registryAccess, environment);
        });
        
        System.out.println("[FLEET TOOLS] Fleet Tools mod initialized with essential commands:");
        System.out.println("[FLEET TOOLS] - /home, /sethome - Home teleportation");
        System.out.println("[FLEET TOOLS] - /spawn, /setspawn - Spawn teleportation");
        System.out.println("[FLEET TOOLS] - /back - Return to previous location");
        System.out.println("[FLEET TOOLS] - /heal - Restore health");
        System.out.println("[FLEET TOOLS] - /feed - Restore hunger");
        System.out.println("[FLEET TOOLS] - /fly - Toggle flight mode");
        System.out.println("[FLEET TOOLS] - /gamemode, /gmc, /gms, /gma, /gmsp - Change game mode");
        System.out.println("[FLEET TOOLS] - /god - Toggle god mode");
    }
}
