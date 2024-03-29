package me.darkeyedragon.randomtp.common.queue;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

/**
 * The common implementation of the queue listener.
 * When a location is consumed another is automatically searched for and added.
 * It also automatically logs to the console if configured to do so.
 */
public class CommonQueueListener implements QueueListener<RandomLocation> {

    private final RandomConfigHandler configHandler;
    private final RandomWorld randomWorld;
    private final LocationQueue locationQueue;
    private final int size;
    private final RandomTeleportPlugin<?> plugin;

    public CommonQueueListener(RandomTeleportPlugin<?> plugin, RandomWorld randomWorld, LocationQueue locationQueue) {
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
        this.randomWorld = randomWorld;
        this.locationQueue = locationQueue;
        this.size = configHandler.getSectionQueue().getSize();
    }

    @Override
    public void onAdd(RandomLocation element) {
        if (configHandler.getSectionDebug().isShowQueuePopulation()) {
            plugin.getLogger().info("Safe location added for " + element.getWorld().getName() + " (" + locationQueue.size() + "/" + size + ")");
        }
    }

    @Override
    public void onRemove(RandomLocation element) {
        ConfigWorld configWorld = configHandler.getSectionWorld().getConfigWorld(randomWorld.getName());
        //TODO find a proper algorithm to determine generation
        if (locationQueue.size() < size / 2) {
            plugin.getWorldHandler().generate(configWorld, randomWorld, locationQueue.remainingCapacity());
        }
        if (configHandler.getSectionDebug().isShowQueuePopulation()) {
            plugin.getLogger().info("Safe location consumed for " + element.getWorld().getName() + " (" + locationQueue.size() + "/" + size + ")");
        }
    }
}
