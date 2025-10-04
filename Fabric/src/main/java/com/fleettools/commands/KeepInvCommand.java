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

public class KeepInvCommand {
    private static final String PERMISSION_KEEPINV = "fleettools.keepinv";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("keepinv")
                .requires(Permissions.require(PERMISSION_KEEPINV, 0)) // Allow all players
                .executes(KeepInvCommand::executeToggleKeepInv));
    }

    private static int executeToggleKeepInv(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

        // Get current keep inventory status (default is true for new players)
        boolean currentStatus = PlayerDataManager.getKeepInventory(player);
        boolean newStatus = !currentStatus;

        // Update the player's keep inventory status
        PlayerDataManager.setKeepInventory(player, newStatus);

        // Send feedback to player
        if (newStatus) {
            player.sendMessage(
                    Text.literal("§aKeep Inventory enabled! You will keep your items on death (but lose XP)."), false);
        } else {
            player.sendMessage(Text.literal("§cKeep Inventory disabled! You will lose your items on death."), false);
        }

        return 1;
    }
}