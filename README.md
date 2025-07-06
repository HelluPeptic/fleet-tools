# Fleet Tools - Essential Commands for Fabric

Fleet Tools is a Fabric mod that brings the most popular and essential commands from EssentialsX to Minecraft Fabric servers. This mod provides server administrators with fundamental teleportation, utility, and administrative commands with full permission support.

## Features

### 🏠 Home System
- **`/home`** - Teleport to your home location
- **`/sethome`** - Set your home at your current location
- Permission: `fleettools.home`, `fleettools.sethome`

### 🌍 Spawn System
- **`/spawn`** - Teleport to the server spawn
- **`/setspawn`** - Set the server spawn at your current location (admin only)
- Permission: `fleettools.spawn`, `fleettools.setspawn`

### ⬅️ Back System
- **`/back`** - Return to your previous location
- Permission: `fleettools.back`

### 💚 Health & Hunger
- **`/heal [player]`** - Restore health to full and clear negative effects
- **`/feed [player]`** - Restore hunger and saturation to full
- Permission: `fleettools.heal`, `fleettools.heal.others`, `fleettools.feed`, `fleettools.feed.others`

### ✈️ Flight System
- **`/fly [player]`** - Toggle flight mode
- Permission: `fleettools.fly`, `fleettools.fly.others`

### 🎮 Game Mode
- **`/gamemode <mode> [player]`** - Change game mode
- **`/gmc [player]`** - Switch to Creative mode
- **`/gms [player]`** - Switch to Survival mode
- **`/gma [player]`** - Switch to Adventure mode
- **`/gmsp [player]`** - Switch to Spectator mode
- Permission: `fleettools.gamemode`, `fleettools.gamemode.others`

### 🛡️ God Mode
- **`/god [player]`** - Toggle invulnerability
- Permission: `fleettools.god`, `fleettools.god.others`

## Installation

1. Make sure you have Fabric Loader installed
2. Download the latest release from the releases page
3. Place the mod file in your `mods` folder
4. Install the required dependencies:
   - Fabric API
   - Fabric Permissions API
   - Collective (for configuration)
5. Restart your server

## Dependencies

- **Fabric API** - Core Fabric API
- **Fabric Permissions API** - Permission system integration
- **Collective** - Configuration framework

## Permissions

Fleet Tools uses the Fabric Permissions API for permission management. All commands have associated permissions that can be managed through compatible permission plugins like LuckPerms.

### Permission Nodes

| Command | Permission Node | Default Level |
|---------|-----------------|---------------|
| `/home` | `fleettools.home` | 0 (all players) |
| `/sethome` | `fleettools.sethome` | 0 (all players) |
| `/spawn` | `fleettools.spawn` | 0 (all players) |
| `/setspawn` | `fleettools.setspawn` | 2 (operators) |
| `/back` | `fleettools.back` | 0 (all players) |
| `/heal` | `fleettools.heal` | 0 (all players) |
| `/heal <player>` | `fleettools.heal.others` | 2 (operators) |
| `/feed` | `fleettools.feed` | 0 (all players) |
| `/feed <player>` | `fleettools.feed.others` | 2 (operators) |
| `/fly` | `fleettools.fly` | 0 (all players) |
| `/fly <player>` | `fleettools.fly.others` | 2 (operators) |
| `/gamemode` | `fleettools.gamemode` | 2 (operators) |
| `/gamemode <mode> <player>` | `fleettools.gamemode.others` | 2 (operators) |
| `/god` | `fleettools.god` | 0 (all players) |
| `/god <player>` | `fleettools.god.others` | 2 (operators) |

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
```

## Support

For issues, suggestions, or contributions, please visit the [GitHub repository](https://github.com/your-repo/fleet-tools).

## License

This project is licensed under the MIT License.
