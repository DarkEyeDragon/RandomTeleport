package me.darkeyedragon.randomtp.api.failsafe;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;

public interface DeathTracker {

    /**
     * @param player the player to add
     * @param time   the time in milliseconds it should be tracked.
     * @return true if added successfully.
     */
    Long add(RandomPlayer player, long time);

    boolean contains(RandomPlayer player);

    /**
     * @param player remove the player from the tracker.
     * @return true if the player is removed. False if non existent or couldn't be removed.
     */
    Long remove(RandomPlayer player);
}
