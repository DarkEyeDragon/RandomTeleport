package me.darkeyedragon.randomtp.common.plugin;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;

public abstract class RandomTeleportPluginImpl implements RandomTeleportPlugin<RandomTeleportPluginImpl>{

    public void init(){
        populateWorldQueue();
    }

    private void populateWorldQueue() {
        RandomConfigHandler configHandler = getConfigHandler();
        getLogger().info("Populating WorldQueue");
        long startTime = System.currentTimeMillis();
        for (RandomWorld world : configHandler.getSectionWorld().getWorlds()) {
            //TODO Figure out how to deal with this
            //WorldUtil.WORLD_MAP.put(Bukkit.getWorld(world.getUUID()), world);
            //Add a new world to the world queue and generate random locations
            LocationQueue locationQueue = new LocationQueue(configHandler.getSectionQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, this));
            //Subscribe to the locationqueue to be notified of changes
            subscribe(locationQueue, world);
            SectionWorldDetail sectionWorldDetail = getLocationFactory().getWorldConfigSection(world);
            locationQueue.generate(sectionWorldDetail);
            getWorldQueue().put(world, locationQueue);
        }
        getLogger().info("WorldQueue population finished in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (getConfigHandler().getSectionDebug().isShowQueuePopulation()) {
            int size = getConfigHandler().getSectionQueue().getSize();
            locationQueue.subscribe(new QueueListener<RandomLocation>() {
                @Override
                public void onAdd(RandomLocation element) {
                    getLogger().info("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                }

                @Override
                public void onRemove(RandomLocation element) {
                    getLogger().info("Safe location consumed for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                }
            });
        }
    }
}
