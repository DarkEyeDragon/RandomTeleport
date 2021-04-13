package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;

/**
 * An interface designed to handle a queue to a single world
 * One {@link RandomWorldHandler} should contain one world
 */
public interface RandomWorldHandler {

    WorldQueue getWorldQueue();

    void populateWorldQueue();

    void populateWorld(ConfigWorld world);

    RandomWorld getWorld(String worldName);
}
