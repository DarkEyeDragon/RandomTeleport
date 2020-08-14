package me.darkeyedragon.randomtp.world;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;


public class WorldQueue{

    private final Map<World, LocationQueue> worldQueueMap;

    public WorldQueue() {
        this.worldQueueMap = new HashMap<>();
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
