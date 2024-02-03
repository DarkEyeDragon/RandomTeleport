package me.darkeyedragon.randomtp.world;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.teleport.TeleportType;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
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
    public CompletableFuture<TeleportResponse> teleportAsync(RandomLocation location) {
        //Teleport the player async if possible
        return PaperLib.teleportAsync(player, WorldUtil.toLocation(location)).thenApply(success -> success ? new BasicTeleportResponse(TeleportType.SUCCESS) : new BasicTeleportResponse(TeleportType.FAIL));
    }

    @Override
    public void teleport(RandomLocation location) {
        player.teleport(WorldUtil.toLocation(location));
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
