package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationDataProvider;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationQueue extends ObservableQueue<RandomLocation> {

    private final RandomTeleportPlugin<?> plugin;
    private final int capacity;
    private final LocationSearcher baseLocationSearcher;

    public LocationQueue(RandomTeleportPlugin<?> plugin, int capacity, LocationSearcher baseLocationSearcher) {
        super(capacity);
        this.plugin = plugin;
        this.capacity = capacity;
        this.baseLocationSearcher = baseLocationSearcher;
    }

    /**
     * Generates locations based on the {@link LocationDataProvider}.
     *
     * @param dataProvider the {@link LocationDataProvider}
     */
    public void generate(LocationDataProvider dataProvider, int amount) {
        AtomicBoolean isSearching = new AtomicBoolean(false);
        AtomicInteger count = new AtomicInteger(amount);
        plugin.getScheduler().runTaskTimer(task -> {
            if (isSearching.get()) return;
            if (count.get() <= 0) {
                task.cancel();
                return;
            }
            isSearching.set(true);
            baseLocationSearcher.getRandom(dataProvider).thenAccept(location -> {
                if (location == null) {
                    //plugin.getLogger().warn("Could not find location for " + dataProvider.getWorld().getName() + "! Retrying...");
                    isSearching.set(false);
                    return;
                } else {
                    isSearching.set(false);
                    count.getAndDecrement();
                }
                offer(location);
            });
        }, plugin.getConfigHandler().getSectionQueue().getInitDelay(), 1);
    }

    public int getCapacity() {
        return capacity;
    }
}