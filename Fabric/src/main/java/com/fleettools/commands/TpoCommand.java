package com.fleettools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import com.fleettools.data.PlayerDataManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class TpoCommand {
    private static final String PERMISSION_TPO = "fleettools.tpo";

    private static final SuggestionProvider<ServerCommandSource> ALL_PLAYERS_SUGGESTIONS = (context, builder) -> {
        MinecraftServer server = context.getSource().getServer();
        
        // Add online players
        server.getPlayerManager().getPlayerList().forEach(player -> {
            builder.suggest(player.getName().getString());
        });
        
        // For offline players, we'll just suggest based on player data files
        File playerDataDir = server.getRunDirectory().toPath()
            .resolve("fleettools")
            .resolve("players")
            .toFile();
            
        if (playerDataDir.exists()) {
            File[] playerFiles = playerDataDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (playerFiles != null) {
                for (File playerFile : playerFiles) {
                    String fileName = playerFile.getName();
                    String uuid = fileName.substring(0, fileName.length() - 5); // Remove .json
                    try {
                        java.util.UUID playerUUID = java.util.UUID.fromString(uuid);
                        GameProfile profile = server.getUserCache().getByUuid(playerUUID).orElse(null);
                        if (profile != null && profile.getName() != null) {
                            // Only add if not already in online players
                            String playerName = profile.getName();
                            boolean isOnline = server.getPlayerManager().getPlayer(playerName) != null;
                            if (!isOnline) {
                                builder.suggest(playerName);
                            }
                        }
                    } catch (Exception ignored) {
                        // Skip invalid UUIDs or profiles
                    }
                }
            }
        }
        
        return CompletableFuture.completedFuture(builder.build());
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        // Register both /tpo and /tpoffline
        dispatcher.register(literal("tpo")
            .requires(Permissions.require(PERMISSION_TPO, 2))
            .then(argument("player", StringArgumentType.word())
                .suggests(ALL_PLAYERS_SUGGESTIONS)
                .executes(TpoCommand::executeTpo))
        );
        
        dispatcher.register(literal("tpoffline")
            .requires(Permissions.require(PERMISSION_TPO, 2))
            .then(argument("player", StringArgumentType.word())
                .suggests(ALL_PLAYERS_SUGGESTIONS)
                .executes(TpoCommand::executeTpo))
        );
    }

    private static int executeTpo(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String targetPlayerName = StringArgumentType.getString(context, "player");
        ServerPlayerEntity sender = context.getSource().getPlayerOrThrow();
        MinecraftServer server = context.getSource().getServer();
        
        // First check if the player is online
        ServerPlayerEntity onlineTarget = server.getPlayerManager().getPlayer(targetPlayerName);
        if (onlineTarget != null) {
            // Player is online, teleport to their current location
            Vec3d targetPos = onlineTarget.getPos();
            ServerWorld targetWorld = onlineTarget.getServerWorld();
            
            // Save sender's current location for /back
            PlayerDataManager.setLastLocation(sender, sender.getPos(), sender.getServerWorld());
            
            // Teleport to online player
            sender.teleport(targetWorld, targetPos.x, targetPos.y, targetPos.z, sender.getYaw(), sender.getPitch());
            sender.sendMessage(Text.literal("§aTeleported to §e" + targetPlayerName + "§a's current location."), false);
            
            return 1;
        }
        
        // Player is offline, try to find their last known location
        GameProfile targetProfile = null;
        
        // Try to find player by name in user cache
        targetProfile = server.getUserCache().findByName(targetPlayerName).orElse(null);
        
        if (targetProfile == null) {
            sender.sendMessage(Text.literal("§cPlayer '" + targetPlayerName + "' not found."), false);
            return 0;
        }
        
        // Try to get the player's last known location from our data using UUID
        try {
            Vec3d lastLocation = PlayerDataManager.getLastLocationByUUID(targetProfile.getId());
            ServerWorld lastWorld = PlayerDataManager.getLastWorldByUUID(targetProfile.getId(), server);
            
            if (lastLocation == null || lastWorld == null) {
                sender.sendMessage(Text.literal("§cNo last known location found for '" + targetPlayerName + "'."), false);
                return 0;
            }
            
            // Save sender's current location for /back
            PlayerDataManager.setLastLocation(sender, sender.getPos(), sender.getServerWorld());
            
            // Teleport to offline player's last location
            sender.teleport(lastWorld, lastLocation.x, lastLocation.y, lastLocation.z, sender.getYaw(), sender.getPitch());
            sender.sendMessage(Text.literal("§aTeleported to §e" + targetPlayerName + "§a's last known location."), false);
            
            return 1;
            
        } catch (Exception e) {
            sender.sendMessage(Text.literal("§cFailed to retrieve location data for '" + targetPlayerName + "'."), false);
            return 0;
        }
    }
}
