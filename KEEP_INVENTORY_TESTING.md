# Keep Inventory System Testing Guide

## Overview

The Fleet Tools mod now includes an opt-out keep inventory system where:

- **All players start with keep inventory ENABLED by default**
- Players can toggle their personal setting with `/keepinv`
- When keep inventory is enabled: items are retained but XP is lost on death
- When keep inventory is disabled: normal vanilla death behavior (lose items and XP)

## Testing Steps

### 1. Test Default Behavior (New Players)

1. Start server with Fleet Tools mod
2. Join as a new player
3. Verify you have keep inventory enabled by default:
   ```
   /keepinv
   ```
   Should show: "Keep inventory is currently ENABLED"

### 2. Test Opt-Out Functionality

1. Disable keep inventory:
   ```
   /keepinv
   ```
   Should show: "Keep inventory is now DISABLED"
2. Enable again:
   ```
   /keepinv
   ```
   Should show: "Keep inventory is now ENABLED"

### 3. Test Death with Keep Inventory ENABLED

1. Ensure keep inventory is enabled: `/keepinv` (should show ENABLED)
2. Gather some items and gain XP levels
3. Die (fall damage, lava, etc.)
4. **Expected behavior:**
   - Items should be retained in inventory
   - XP levels should be reset to 0
   - Respawn with items but no XP

### 4. Test Death with Keep Inventory DISABLED

1. Disable keep inventory: `/keepinv` (should show DISABLED)
2. Gather some items and gain XP levels
3. Die (fall damage, lava, etc.)
4. **Expected behavior:**
   - Items should drop at death location (vanilla behavior)
   - XP should drop as orbs at death location
   - Respawn with empty inventory and 0 XP

### 5. Test Persistence

1. Set keep inventory preference (enabled or disabled)
2. Disconnect from server
3. Reconnect to server
4. Verify setting is preserved: `/keepinv`

## Implementation Details

### Components

- **KeepInvCommand.java**: Command to toggle per-player setting
- **ServerPlayerEntityKeepInvMixin.java**: Mixin to handle death behavior
- **KeepInventoryHandler.java**: Event handler for XP reset
- **PlayerDataManager.java**: Persistent data storage

### Technical Approach

1. **Mixin System**: Temporarily enables keepInventory gamerule during death processing for players with keep inventory enabled
2. **Event System**: Resets XP to 0 after death for players with keep inventory enabled
3. **Data Persistence**: Player preferences saved to JSON files

## Troubleshooting

### Common Issues

1. **Server Crash on Startup**: Check mixin compatibility
2. **Items Not Kept**: Verify player has keep inventory enabled
3. **XP Not Reset**: Check event handler registration
4. **Settings Not Saved**: Verify JSON write permissions

### Debug Commands

- `/keepinv` - Check current setting
- Check server logs for mixin loading
- Verify player data files in `world/fleettools/players/`

## Performance Notes

- Minimal performance impact
- Mixin only activates during player death events
- JSON I/O only on setting changes and server startup
