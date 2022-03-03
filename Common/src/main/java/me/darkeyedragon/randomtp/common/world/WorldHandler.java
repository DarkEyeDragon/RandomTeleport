package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;
import me.darkeyedragon.randomtp.api.world.location.search.LocationDataProvider;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.config.datatype.Offset;
import me.darkeyedragon.randomtp.common.queue.CommonQueueListener;
import me.darkeyedragon.randomtp.common.world.location.search.CommonLocationDataProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class WorldHandler implements RandomWorldHandler {

    private final WorldQueue worldQueue;
    private final RandomTeleportPlugin<?> plugin;
    private static final Map<RandomEnvironment, LocationSearcher> locationSearcherMap = new HashMap<>();


    public WorldHandler(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
        this.worldQueue = new WorldQueue();
    }

    public static LocationSearcher registerLocationSearcher(RandomEnvironment environment, LocationSearcher locationSearcher) {
        return locationSearcherMap.put(environment, locationSearcher);
    }

    public static LocationSearcher getLocationSearcher(RandomEnvironment environment) {
        return locationSearcherMap.get(environment);
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
        RandomConfigHandler configHandler = plugin.getConfigHandler();
        RandomWorld world = getWorld(configWorld.getName());
        if (world == null) {
            plugin.getLogger().warn("World " + configWorld.getName() + " does not exist! Skipping...");
            return;
        }
        if (getWorldQueue().get(world) != null) {
            plugin.getLogger().warn("World " + world.getName() + " is already loaded. Skipping...");
            return;
        }
        plugin.getLogger().info("Found \"" + world.getName() + "\". Loading...");
        LocationQueue locationQueue = new LocationQueue(plugin, configHandler.getSectionQueue().getSize(), getLocationSearcher(world.getEnvironment()));

        //Subscribe to the locationqueue to be notified of changes
        subscribe(locationQueue, world);
        getWorldQueue().put(world, locationQueue);
        plugin.getLogger().info("Loaded \"" + world.getName() + "\"");
        generate(configWorld, world, configHandler.getSectionQueue().getSize());
    }

    private LocationDataProvider createDataProvider(ConfigWorld configWorld, RandomWorld world) {
        RandomOffset offset;
        int radius;
        if (configWorld.isUseWorldborder()) {
            RandomWorldBorder worldBorder = world.getWorldBorder();
            RandomLocation location = worldBorder.getCenter();
            offset = new Offset(location.getBlockX(), location.getBlockZ());
            //keep warning distance of the worldborder in mind
            radius = (int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance());
        } else {
            offset = configWorld.getConfigWorldborder().getOffset();
            radius = configWorld.getConfigWorldborder().getRadius();
        }
        return new CommonLocationDataProvider(world, offset, radius);
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (plugin.getConfigHandler().getSectionDebug().isShowQueuePopulation()) {
            CommonQueueListener queueListener = new CommonQueueListener(plugin, world, locationQueue);
            locationQueue.subscribe(queueListener);
        }
    }

    public void generate(ConfigWorld configWorld, RandomWorld randomWorld) {
        getWorldQueue().get(randomWorld).generate(createDataProvider(configWorld, randomWorld), 1);
    }

    public void generate(ConfigWorld configWorld, RandomWorld randomWorld, int size) {
        getWorldQueue().get(randomWorld).generate(createDataProvider(configWorld, randomWorld), size);
    }

    @Override
    public abstract RandomWorld getWorld(String worldName);

    @Override
    public WorldQueue getWorldQueue() {
        return worldQueue;
    }

    public RandomTeleportPlugin<?> getPlugin() {
        return plugin;
    }
}
