package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.teleport.TeleportType;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.teleport.BasicTeleportResponse;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerSpigot implements RandomPlayer {

    private final Player player;
    private RandomCooldown cooldown;

    public PlayerSpigot(Player player) {
        this.player = player;
    }

    public PlayerSpigot(Player player, RandomCooldown cooldown) {
        this.player = player;
        this.cooldown = cooldown;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public RandomLocation getLocation() {
        return WorldUtil.toRandomLocation(player.getLocation());
    }

    @Override
    public RandomWorld getWorld() {
        return WorldUtil.toRandomWorld(player.getWorld());
    }

    @Override
    public RandomLocation getEyeLocation() {
        return WorldUtil.toRandomLocation(player.getEyeLocation());
    }

@Override
public CompletableFuture<TeleportResponse> teleportAsync(TeleportProperty teleportProperty) {
    RandomLocation randomLocation = teleportProperty.getLocation();
    //Teleport the player async if possible
    /*return PaperLib.teleportAsync(player, WorldUtil.toLocation(randomLocation)).thenApply(aBoolean -> {
        if (teleportProperty.getInitTime() != 0) {
            Logger logger = LogManager.getLogManager().getLogger("RandomTeleport");
            logger.info("Debug: total teleport time took: " + (System.currentTimeMillis() - teleportProperty.getInitTime()) + "ms");
        }
        if (aBoolean) {
            return new BasicTeleportResponse(TeleportType.SUCCESS);
        } else {
            return new BasicTeleportResponse(TeleportType.FAIL);
        }
    });*/
    //TODO TEMP FIX
    boolean complete = player.teleport(WorldUtil.toLocation(randomLocation));
    if (complete) {
        return CompletableFuture.completedFuture(new BasicTeleportResponse(TeleportType.SUCCESS));
    }
    return CompletableFuture.completedFuture(new BasicTeleportResponse(TeleportType.FAIL));
}

    @Override
    public RandomCooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(RandomCooldown cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
