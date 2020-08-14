package me.darkeyedragon.randomtp.api.queue;


import java.util.HashMap;
import java.util.Map;

public class WorldQueue<K, V extends ObservableQueue<V>> {

    private final Map<K, V> worldQueueMap;

    public WorldQueue() {
        this.worldQueueMap = new HashMap<>();
    }

    public V put(K world, V locationQueue) {
        return worldQueueMap.put(world, locationQueue);
    }

    public V remove(K world) {
        return worldQueueMap.remove(world);
    }

    public V get(K world) {
        return worldQueueMap.get(world);
    }

    public void clear() {
        worldQueueMap.clear();
    }

    public V popLocation(K world) {
        V locationQueue = get(world);
        if (locationQueue == null) {
            return null;
        }
        return locationQueue.poll();
    }
}
