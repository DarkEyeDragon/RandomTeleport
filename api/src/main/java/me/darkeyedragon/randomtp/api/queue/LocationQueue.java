package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

public class LocationQueue extends ObservableQueue<RandomLocation> {

    private final LocationSearcher baseLocationSearcher;

    public LocationQueue(int capacity, LocationSearcher baseLocationSearcher) {
        super(capacity);
        this.baseLocationSearcher = baseLocationSearcher;
    }

    public boolean offer(RandomLocation location) {
        return super.offer(location);
    }

    public RandomLocation poll() {
        return super.poll();
    }

    public void generate(SectionWorldDetail sectionWorldDetail) {
        generate(sectionWorldDetail, super.remainingCapacity());
    }
    public void generate(SectionWorldDetail sectionWorldDetail, int amount){
        for(int i = 0; i < amount; i++){
            baseLocationSearcher.getRandom(sectionWorldDetail).thenAccept(this::offer);
        }
    }
}
