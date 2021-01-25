package me.darkeyedragon.randomtp.world;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
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
    public RandomLocation getLocation() {
        return WorldUtil.toRandomLocation(player.getLocation());
    }

    @Override
    public RandomLocation getEyeLocation() {
        return WorldUtil.toRandomLocation(player.getEyeLocation());
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(TeleportProperty teleportProperty) {
        RandomLocation randomLocation = teleportProperty.getLocation();
        //Teleport the player async if possible
        return PaperLib.teleportAsync(player, WorldUtil.toLocation(randomLocation));
    }

    @Override
    public RandomCooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(RandomCooldown cooldown) {
        this.cooldown = cooldown;
    }
}
