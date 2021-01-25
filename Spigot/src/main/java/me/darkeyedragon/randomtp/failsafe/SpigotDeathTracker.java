package me.darkeyedragon.randomtp.failsafe;

import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SpigotDeathTracker implements DeathTracker {

    RandomTeleportPlugin<?> randomTeleport;
    Map<RandomPlayer, BukkitTask> trackedPlayers;

    public SpigotDeathTracker(RandomTeleportPlugin<?> plugin) {
        this.randomTeleport = plugin;
        trackedPlayers = new HashMap<>();
    }

    /**
     * @param player the {@link Player} that you want to track
     * @param time   the amount of time before the player is removed again.
     */
    public void add(Player player, long time) {
        trackedPlayers.put(player, Bukkit.getScheduler().runTaskLater(randomTeleport.getPlugin(), () -> remove(player), time));
    }

    public boolean contains(Player player) {
        return trackedPlayers.containsKey(player);
    }

    public BukkitTask getBukkitTask(Player player) {
        return trackedPlayers.get(player);
    }

    public void remove(Player player) {
        trackedPlayers.remove(player);
    }

    @Override
    public void add(RandomPlayer player, long time) {

    }

    @Override
    public boolean contains(RandomPlayer player) {
        return false;
    }

    @Override
    public void remove(RandomPlayer player) {
        trackedPlayers.remove(player);
    }
}
