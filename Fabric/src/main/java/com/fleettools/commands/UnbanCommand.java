package com.fleettools.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.server.ServerConfigEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.BannedPlayerEntry;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class UnbanCommand {
    private static final String PERMISSION = "fleettools.unban";

    private static final SuggestionProvider<ServerCommandSource> BANNED_PLAYERS_SUGGESTIONS = (context, builder) -> {
        BannedPlayerList bannedPlayers = context.getSource().getServer().getPlayerManager().getUserBanList();
        for (String name : bannedPlayers.getNames()) {
            builder.suggest(name);
        }
        return CompletableFuture.completedFuture(builder.build());
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("unban")
            .requires(Permissions.require(PERMISSION, 3))
            .then(argument("player", StringArgumentType.word())
                .suggests(BANNED_PLAYERS_SUGGESTIONS)
                .executes(UnbanCommand::executeUnban))
        );
    }

    private static int executeUnban(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String playerName = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getServer();
        
        // Try to find the player profile by name
        GameProfile profile = server.getUserCache().findByName(playerName).orElse(null);
        if (profile == null) {
            context.getSource().sendError(Text.literal("§cPlayer '" + playerName + "' not found."));
            return 0;
        }
        
        BannedPlayerList bannedPlayers = server.getPlayerManager().getUserBanList();
        if (!bannedPlayers.contains(profile)) {
            context.getSource().sendError(Text.literal("§cPlayer '" + playerName + "' is not banned."));
            return 0;
        }
        
        bannedPlayers.remove(profile);
        context.getSource().sendFeedback(() -> Text.literal("§aUnbanned player '" + playerName + "'."), true);
        
        return 1;
    }
}
