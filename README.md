# Random Teleport
Teleport a player to a random (safe) location in the Minecraft world.

### Features
- World based teleports.
- Teleport cooldown.
- Pre generated random locations (instant teleports).
- Adjustable queue. Determine yourself how many pre generated locations you want.
- Everything customizable.
- Full async chunkloading support for Paper servers.
- Choose if a world requires a permission or not

### Permissions
|Permission|Description|
|----------|-------|
|``rtp.teleport.self``| Allows you to use the `/rtp` base command |
|``rtp.teleport.other``| Allows you to teleport other players with `/rtp <playername>`|
|``rtp.teleport.bypass``| Lets you to bypass the cooldown timer|
|``rtp.teleport.world``| Random teleport to any world that doesnt require extra permissions
|``rtp.teleport.*``| Give all permissions. Including bypass|

### For the more tech savvy people the plugin.yml permissions

```yml
permissions:
  rtp.teleport.*:
    description: give all teleport permissions
    children:
      rtp.teleport.self: true
      rtp.teleport.other: true
      rtp.teleport.bypass: true
      rtp.teleport.world: true
  rtp.*:
    description: give all rtp commands
    children:
      rtp.teleport.*: true
      rtp.teleportdelay.bypass: true
  rtp.teleport.self:
    description: random teleport yourself
  rtp.teleport.other:
    description: random teleport others
  rtp.teleport.world:
    description: random teleport to any world that doesnt require extra permissions
  rtp.teleport.bypass:
    description: bypass the cooldown
  rtp.teleportdelay.bypass:
    description: bypass initial tp delay
```

### Known issues
- Config files will lose comments when adding/removing worlds through commands (SnakeYML limitation). Might or might not fix.
- Setting a radius with all unsafe locations will cause the plugin to search indefinitely. There is no search limit.
