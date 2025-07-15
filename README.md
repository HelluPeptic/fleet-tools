# Fleet Tools - Essential Commands for Fabric

## Recent Changes

### 1.20.1+ Compatibility

- All commands and features are now fully compatible with Minecraft 1.20.1 and Fabric Loader 0.15.7+.

### Command Feedback

- Command feedback suppression for operators has been removed. All command feedback now follows vanilla behavior (ops will see feedback for commands they have permission for).

### Teleportation & /tp Command

- Fleet Tools now overrides the `/tp` command to ensure only the executor receives feedback, but this feature can be toggled or removed as needed.
- The `/tp` command supports all vanilla argument patterns: `/tp <player> <destination>`, `/tp <destination>`, `/tp <player> <x> <y> <z>`, `/tp <x> <y> <z>`.
- Teleportation events are tracked for `/back` support.

### Warps System

- EssentialsX-style `/warp`, `/setwarp`, and `/delwarp` commands have been added.
- Warps are stored persistently in `fleettools/warps.json`.

### Permissions

- All commands require explicit permissions (see table below) and are operator-only by default.
- LuckPerms and Fabric Permissions API are supported for fine-grained control.

### Bug Fixes & Refactoring

- Fixed build-breaking issues with misplaced code and mixin targets.
- Improved data storage and error handling for player and global data.

---

Fleet Tools is a Fabric mod that brings the most popular and essential commands from EssentialsX to Minecraft Fabric servers. This mod provides server administrators with fundamental teleportation, utility, and administrative commands with full permission support.

## Features

### Home System

- **`/home`** - Teleport to your home location
- **`/sethome`** - Set your home at your current location
- Permission: `fleettools.home`, `fleettools.sethome` (default: operators only)

### Spawn System

- **`/spawn`** - Teleport to the server spawn
- **`/setspawn`** - Set the server spawn at your current location (admin only)
- Permission: `fleettools.spawn`, `fleettools.setspawn` (default: operators only)

### Back System

- **`/back`** - Return to your previous location
- Permission: `fleettools.back` (default: operators only)

### Health & Hunger

- **`/heal [player]`** - Restore health to full and clear negative effects
- **`/feed [player]`** - Restore hunger and saturation to full
- Permission: `fleettools.heal`, `fleettools.heal.others`, `fleettools.feed`, `fleettools.feed.others` (default: operators only)

### Flight System

- **`/fly [player]`** - Toggle flight mode
- Permission: `fleettools.fly`, `fleettools.fly.others` (default: operators only)

### Game Mode

- **`/gamemode <mode> [player]`** - Change game mode
- **`/gmc [player]`** - Switch to Creative mode
- **`/gms [player]`** - Switch to Survival mode
- **`/gma [player]`** - Switch to Adventure mode
- **`/gmsp [player]`** - Switch to Spectator mode
- Permission: `fleettools.gamemode`, `fleettools.gamemode.others` (default: operators only)

### God Mode

- **`/god [player]`** - Toggle invulnerability
- Permission: `fleettools.god`, `fleettools.god.others` (default: operators only)

### Warps System

- **`/warp <name>`** - Teleport to a named warp location
- **`/setwarp <name>`** - Set a warp at your current location
- **`/delwarp <name>`** - Delete a named warp
- Permission: `fleettools.warp`, `fleettools.setwarp`, `fleettools.delwarp` (default: operators only)

## Installation

1. Make sure you have Fabric Loader installed
2. Download the latest release from the releases page
3. Place the mod file in your `mods` folder
4. Install the required dependencies:
   - Fabric API
   - Fabric Permissions API
5. Restart your server

## Dependencies

- **Fabric API** - Core Fabric API
- **Fabric Permissions API** - Permission system integration

## Permissions

Fleet Tools uses the Fabric Permissions API for permission management. All commands have associated permissions that can be managed through compatible permission plugins like LuckPerms.

### Permission Nodes

| Command                     | Permission Node              | Default Level |
| --------------------------- | ---------------------------- | ------------- |
| `/home`                     | `fleettools.home`            | 2 (operators) |
| `/sethome`                  | `fleettools.sethome`         | 2 (operators) |
| `/spawn`                    | `fleettools.spawn`           | 2 (operators) |
| `/setspawn`                 | `fleettools.setspawn`        | 2 (operators) |
| `/back`                     | `fleettools.back`            | 2 (operators) |
| `/heal`                     | `fleettools.heal`            | 2 (operators) |
| `/heal <player>`            | `fleettools.heal.others`     | 2 (operators) |
| `/feed`                     | `fleettools.feed`            | 2 (operators) |
| `/feed <player>`            | `fleettools.feed.others`     | 2 (operators) |
| `/fly`                      | `fleettools.fly`             | 2 (operators) |
| `/fly <player>`             | `fleettools.fly.others`      | 2 (operators) |
| `/gamemode`                 | `fleettools.gamemode`        | 2 (operators) |
| `/gamemode <mode> <player>` | `fleettools.gamemode.others` | 2 (operators) |
| `/god`                      | `fleettools.god`             | 2 (operators) |
| `/god <player>`             | `fleettools.god.others`      | 2 (operators) |
| `/warp`                     | `fleettools.warp`            | 2 (operators) |
| `/setwarp`                  | `fleettools.setwarp`         | 2 (operators) |
| `/delwarp`                  | `fleettools.delwarp`         | 2 (operators) |

## Data Storage

Fleet Tools stores player data in JSON files in the `fleettools` folder within your server directory:

- `fleettools/players/` - Individual player data (homes, last locations, etc.)
- `fleettools/global.json` - Global server data (spawn location, etc.)

## Compatibility

- **Minecraft Version**: 1.20.1
- **Fabric Loader**: 0.12.5+
- **Java**: 17+

## Configuration

The mod automatically creates necessary data files and folders on first run. No additional configuration is required.

## Commands Reference

### Basic Usage Examples

```
/home              # Teleport to your home
/sethome           # Set home at current location
/spawn             # Teleport to spawn
/setspawn          # Set spawn (admin only)
/back              # Return to previous location
/heal              # Heal yourself
/heal Steve        # Heal another player (admin only)
/feed              # Feed yourself
/feed Steve        # Feed another player (admin only)
/fly               # Toggle flight for yourself
/fly Steve         # Toggle flight for another player (admin only)
/gamemode creative # Change to creative mode
/gmc               # Quick creative mode
/gms               # Quick survival mode
/god               # Toggle god mode
/warp myWarp       # Teleport to a warp named 'myWarp'
/setwarp myWarp    # Set a warp named 'myWarp' at current location
/delwarp myWarp    # Delete the warp named 'myWarp'
```

## Support

For issues, suggestions, or contributions, please visit the [GitHub repository](https://github.com/your-repo/fleet-tools).

## License

This project is licensed under the MIT License.
