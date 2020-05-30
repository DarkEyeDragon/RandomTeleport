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
      rtp.eco.*: true
  rtp.eco.*:
    description: bypass all economy restrictions
    children:
      rtp.eco.bypass: true
  rtp.admin.*:
    description: give all admin commands
    children:
      rtp.admin.removeworld: true
      rtp.admin.addworld: true
      rtp.admin.resetcooldown: true
      rtp.admin.reload: true
      rtp.admin.setprice: true
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
  rtp.eco.bypass:
    description: bypass economy costs
  rtp.admin.removeworld:
    description: allow admins to remove worlds from the config
  rtp.admin.addworld:
    description: allow admins to add worlds to the config
  rtp.admin.resetcooldown:
    description: allow admins to reset the cooldown timer of players
  rtp.admin.reload:
    description: allow admins to reload the plugin configs and repopulate queues
  rtp.admin.setprice:
    description: allow admins to set the teleport costs
```
![alt text](https://i.imgur.com/78pXgKp.png "commands")
![alt text](https://i.imgur.com/dhdUE8i.png "rtp")
![alt text](https://i.imgur.com/9JCz30l.png "async world ")
### Known issues
- Config files will lose comments when adding/removing worlds through commands (SnakeYML limitation). Might or might not fix.
- Setting a radius with all unsafe locations will cause the plugin to search indefinitely. There is no search limit.
