package me.darkeyedragon.randomtp.api.failsafe;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;

public interface DeathTracker {

    void add(RandomPlayer player, long time);

    boolean contains(RandomPlayer player);

    void remove(RandomPlayer player);
}
