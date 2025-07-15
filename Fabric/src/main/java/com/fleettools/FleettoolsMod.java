package com.fleettools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.fleettools.commands.*;
import com.fleettools.events.PlayerJoinHandler;
import com.fleettools.data.PlayerDataManager;

public class FleettoolsMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("[FLEET TOOLS] Initializing Fleet Tools mod - FULL VERSION");
        
        // Initialize player data manager and warps when server starts
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            PlayerDataManager.init(server);
            PlayerDataManager.loadWarps(server);
        });
        
        // Register commands that exist
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BackCommand.register(dispatcher, registryAccess, environment);
            HomeCommand.register(dispatcher, registryAccess, environment);
            SpawnCommand.register(dispatcher, registryAccess, environment);
            HealCommand.register(dispatcher, registryAccess, environment);
            GodCommand.register(dispatcher, registryAccess, environment);
            GamemodeCommand.register(dispatcher, registryAccess, environment);
            FlyCommand.register(dispatcher, registryAccess, environment);
            FeedCommand.register(dispatcher, registryAccess, environment);
            DelHomeCommand.register(dispatcher, registryAccess, environment);
            WarpCommand.register(dispatcher, registryAccess, environment);
        });
        
        // Register event handlers
        PlayerJoinHandler.register();

        
        System.out.println("[FLEET TOOLS] All features enabled - Commands, Events, Data Management");
    }
}
