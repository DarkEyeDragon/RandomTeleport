package me.darkeyedragon.randomtp.failsafe;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class DeathTracker {

    RandomTeleport randomTeleport;
    Map<Player, BukkitTask> trackedPlayers;

    public DeathTracker(RandomTeleport randomTeleport) {
        this.randomTeleport = randomTeleport;
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
}
