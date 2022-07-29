package me.darkeyedragon.randomtp.common.failsafe;

import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

import java.util.HashMap;
import java.util.Map;

public class CommonDeathTracker implements DeathTracker {

    RandomTeleportPlugin<?> randomTeleport;
    Map<RandomPlayer, Long> trackedPlayers;

    public CommonDeathTracker(RandomTeleportPlugin<?> plugin) {
        this.randomTeleport = plugin;
        trackedPlayers = new HashMap<>();
    }


    @Override
    public Long add(RandomPlayer player, long time) {
        return trackedPlayers.put(player, time);
    }

    @Override
    public boolean contains(RandomPlayer player) {
        return trackedPlayers.containsKey(player);
    }

    @Override
    public Long remove(RandomPlayer player) {
        return trackedPlayers.remove(player);
    }
}
