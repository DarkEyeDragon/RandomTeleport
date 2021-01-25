package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;

public abstract class WorldHandler implements RandomWorldHandler {

    private final WorldQueue worldQueue;
    private final RandomTeleportPlugin<?> plugin;

    public WorldHandler(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        worldQueue = new WorldQueue();
    }

    @Override
    public WorldQueue getWorldQueue() {
        return worldQueue;
    }

    @Override
    public void populateWorldQueue() {
        RandomConfigHandler configHandler = plugin.getConfigHandler();
        plugin.getLogger().info("Populating WorldQueue");
        long startTime = System.currentTimeMillis();
        for (RandomWorld world : configHandler.getSectionWorld().getWorlds()) {
            //Add a new world to the world queue and generate random locations
            LocationQueue locationQueue = new LocationQueue(configHandler.getSectionQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, this));

            //Subscribe to the locationqueue to be notified of changes
            subscribe(locationQueue, world);
            SectionWorldDetail sectionWorldDetail = getLocationFactory().getWorldConfigSection(world);
            locationQueue.generate(sectionWorldDetail);
            worldQueue.put(world, locationQueue);
        }
        plugin.getLogger().info("WorldQueue population finished in " + (System.currentTimeMillis() - startTime) + "ms");

    }

    @Override
    public abstract RandomWorld getWorld(String worldName);
}
