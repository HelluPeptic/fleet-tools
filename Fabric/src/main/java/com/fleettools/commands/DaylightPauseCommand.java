package com.fleettools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import net.minecraft.server.world.ServerWorld;

import java.util.Timer;
import java.util.TimerTask;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class DaylightPauseCommand {
    private static final String PERMISSION = "fleettools.daylightpause";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("daylightpause")
            .requires(Permissions.require(PERMISSION, 2))
            .then(argument("minutes", IntegerArgumentType.integer(1, 1440))
                .executes(DaylightPauseCommand::executePause))
        );
    }

    private static int executePause(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int minutes = IntegerArgumentType.getInteger(context, "minutes");
        MinecraftServer server = context.getSource().getServer();
        // Pause daylight cycle
        for (ServerWorld world : server.getWorlds()) {
            world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
        }
        context.getSource().sendFeedback(() -> Text.literal("Daylight cycle paused for " + minutes + " minute(s)."), false);
        // Schedule resume
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (ServerWorld world : server.getWorlds()) {
                    world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(true, server);
                }
                server.getPlayerManager().broadcast(Text.literal("Daylight cycle resumed."), false);
            }
        }, minutes * 60 * 1000L); // ms
        return 1;
    }
}
