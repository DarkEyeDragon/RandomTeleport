package me.darkeyedragon.randomtp.common.plugin;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;

import java.util.Set;

public abstract class RandomTeleportPluginImpl implements RandomTeleportPlugin{

    public void init(){
        populateWorldQueue();
    }

    public void populateWorldQueue() {
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

}
