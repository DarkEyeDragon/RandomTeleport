package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationDataProvider;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

public class LocationQueue extends ObservableQueue<RandomLocation> {

    private final LocationSearcher baseLocationSearcher;

    public LocationQueue(int capacity, LocationSearcher baseLocationSearcher) {
        super(capacity);
        this.baseLocationSearcher = baseLocationSearcher;
    }

    public void generate(LocationDataProvider dataProvider) {
        generate(dataProvider, super.remainingCapacity());
    }

    public void generate(LocationDataProvider dataProvider, int amount) {
        for (int i = 0; i < amount; i++) {
            baseLocationSearcher.getRandom(dataProvider).thenAccept(super::offer);
        }
    }
}
