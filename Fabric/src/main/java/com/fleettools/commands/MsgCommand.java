package com.fleettools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class MsgCommand {
    private static final String PERMISSION = "fleettools.msg";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("msg")
            .requires(Permissions.require(PERMISSION, 2))
            .then(argument("target", EntityArgumentType.player())
                .then(argument("message", StringArgumentType.greedyString())
                    .executes(MsgCommand::executeMsg)))
        );
    }

    private static int executeMsg(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sender = context.getSource().getPlayerOrThrow();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        String message = StringArgumentType.getString(context, "message");
        
        if (sender == target) {
            sender.sendMessage(Text.literal("§cYou cannot send a message to yourself."), false);
            return 0;
        }
        
        // Send message to target
        target.sendMessage(Text.literal("§7[§e" + sender.getName().getString() + " → You§7] §f" + message), false);
        
        // Send confirmation to sender
        sender.sendMessage(Text.literal("§7[§eYou → " + target.getName().getString() + "§7] §f" + message), false);
        
        return 1;
    }
}
