package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.data.PluginInstance;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationQueueTest implements PluginInstance {

    static BaseLocationSearcher baseLocationSearcher;
    static LocationQueue locationQueue;
    static QueueListener<RandomLocation> queueListener;
    static boolean added = false;
    static int initialCapacity = 5;

    @BeforeAll
    static void setup() {
        baseLocationSearcher = new BaseLocationSearcher(PluginInstance.instance) {
        };
        locationQueue = new LocationQueue(initialCapacity, baseLocationSearcher);
        queueListener = new QueueListener<RandomLocation>() {
            @Override
            public void onAdd(RandomLocation element) {
                added = true;
            }

            @Override
            public void onRemove(RandomLocation element) {
                added = false;
            }
        };
    }

    @Test
    void pollIsSameAsOffer() {
        locationQueue.clear();
        RandomLocation location = new RandomLocation(null, 0, 0, 0);
        locationQueue.offer(location);
        assertEquals(location, locationQueue.poll());
        locationQueue.clear();
    }

    @Test
    void notifyListeners() {
        RandomLocation location = new RandomLocation(null, 0, 0, 0);
        locationQueue.subscribe(queueListener);
        locationQueue.offer(location);
        assertTrue(added);
        assertEquals(location, locationQueue.poll());
        assertFalse(added);
        locationQueue.unsubscribe(queueListener);
    }

    @Test
    void getRemainingCapacity() {
        RandomLocation location = new RandomLocation(null, 0, 0, 0);
        locationQueue.offer(location);
        assertEquals(initialCapacity - 1, locationQueue.remainingCapacity());
    }
}
