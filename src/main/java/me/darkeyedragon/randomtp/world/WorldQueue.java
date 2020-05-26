package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.location.LocationSearcher;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class WorldQueue{

    private final Map<World, LocationQueue> worldQueueMap;
    private final LocationSearcher locationSearcher;

    public WorldQueue(LocationSearcher locationSearcher) {
        this.worldQueueMap = new HashMap<>();
        this.locationSearcher = locationSearcher;
    }

    public void populate(Collection<WorldConfigSection> worldConfigSections, int amount) {
        for (WorldConfigSection worldConfig : worldConfigSections) {
            put(worldConfig, amount);
        }
    }

    public LocationQueue put(World world, LocationQueue locationQueue) {
        return worldQueueMap.put(world, locationQueue);
    }

    public void put(WorldConfigSection worldConfigSection, int amount) {
        LocationQueue locationQueue = new LocationQueue(amount, locationSearcher);
        locationQueue.generate(worldConfigSection);
        worldQueueMap.put(worldConfigSection.getWorld(), locationQueue);
    }

    public LocationQueue get(World world) {
        return worldQueueMap.get(world);
    }

    public void clear() {
        worldQueueMap.clear();
    }

    public Location popLocation(World world) throws InterruptedException {
        return worldQueueMap.get(world).poll();
    }
}
