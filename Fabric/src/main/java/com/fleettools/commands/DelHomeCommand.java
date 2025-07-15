package com.fleettools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.fleettools.data.PlayerDataManager;

import static net.minecraft.server.command.CommandManager.literal;

public class DelHomeCommand {
    private static final String PERMISSION_DELHOME = "fleettools.delhome";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("delhome")
                .requires(Permissions.require(PERMISSION_DELHOME, 2))
                .executes(DelHomeCommand::executeDelHome));
    }

    private static int executeDelHome(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        boolean removed = PlayerDataManager.removeHome(player);
        if (removed) {
            player.sendMessage(Text.literal("§a[FleetTools] Your home has been deleted."), false);
            return 1;
        } else {
            player.sendMessage(Text.literal("§c[FleetTools] You don't have a home set."), false);
            return 0;
        }
    }
}
