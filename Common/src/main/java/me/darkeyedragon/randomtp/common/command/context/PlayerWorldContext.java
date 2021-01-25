package me.darkeyedragon.randomtp.common.command.context;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

import java.util.ArrayList;
import java.util.List;

public class PlayerWorldContext {
    private final List<RandomPlayer> players;
    private RandomWorld world;

    public PlayerWorldContext() {
        players = new ArrayList<>(1);
    }

    public boolean isWorld() {
        return world != null;
    }

    public List<RandomPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(RandomPlayer player) {
        players.add(player);
    }

    public RandomWorld getWorld() {
        return world;
    }

    public void setWorld(RandomWorld world) {
        this.world = world;
    }
}
