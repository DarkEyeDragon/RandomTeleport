package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.location.LocationSearcher;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;


public class WorldQueue{

    private final Map<World, LocationQueue> worldQueueMap;
    private final LocationSearcher locationSearcher;

    public WorldQueue(LocationSearcher locationSearcher) {
        this.worldQueueMap = new HashMap<>();
        this.locationSearcher = locationSearcher;
    }

    public LocationQueue put(World world, LocationQueue locationQueue) {
        return worldQueueMap.put(world, locationQueue);
    }

    public LocationQueue remove(World world){
        return worldQueueMap.remove(world);
    }

    public LocationQueue get(World world) {
        return worldQueueMap.get(world);
    }

    public void clear() {
        worldQueueMap.clear();
    }

    public Location popLocation(World world){
        LocationQueue locationQueue = get(world);
        if(locationQueue == null){
            return null;
        }
        return locationQueue.poll();
    }

}
