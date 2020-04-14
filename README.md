# Random Teleport
Teleport a player to a random (safe) location in the Minecraft world.
### Permissions
|Permission|Description|
|----------|-------|
|``rtp.teleport.self``| Allows you to use the `/rtp` base command |
|``rtp.teleport.other``| Allows you to teleport other players with `/rtp <playername>`|
|``rtp.teleport.bypass``| Lets you to bypass the cooldown timer|
|``rtp.teleport.world``| Allows you to teleport to another world than they are currently in|
|``rtp.world.bypass``| Allows you to bypass the blacklist |
|``rtp.teleport.*``| Give all permissions. Including bypasses|

### Versions

#### version 1.1
- Added a queue system, now a location is always ready when needed. 
- Vastly improved location searching
- When reloading the config you the queue gets repopulated with the new parameters
- Reformatted the config (it is highly recommended to generate a new one!)