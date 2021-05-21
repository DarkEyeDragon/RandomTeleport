package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.common.queue.CommonQueueListener;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;

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
    public final void populateWorldQueue() {
        RandomConfigHandler configHandler = plugin.getConfigHandler();
        plugin.getLogger().info("Populating WorldQueue");
        long startTime = System.currentTimeMillis();
        for (ConfigWorld configWorld : configHandler.getSectionWorld().getConfigWorlds()) {
            populateWorld(configWorld);
        }
        plugin.getLogger().info("WorldQueue population finished in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public final void populateWorld(ConfigWorld configWorld) {
        //Add a new world to the world queue and generate random locations
        RandomConfigHandler configHandler = plugin.getConfigHandler();
        RandomWorld world = getWorld(configWorld.getName());
        if (world == null) {
            plugin.getLogger().warn("World " + configWorld.getName() + " does not exist! Skipping...");
            return;
        }
        LocationQueue locationQueue = new LocationQueue(plugin, configHandler.getSectionQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, plugin));

        //Subscribe to the locationqueue to be notified of changes
        subscribe(locationQueue, world);
        locationQueue.generate(configWorld);
        plugin.getWorldHandler().getWorldQueue().put(world, locationQueue);
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (plugin.getConfigHandler().getSectionDebug().isShowQueuePopulation()) {
            CommonQueueListener queueListener = new CommonQueueListener(plugin, world, locationQueue);
            locationQueue.subscribe(queueListener);
        }
    }

    @Override
    public abstract RandomWorld getWorld(String worldName);
}
