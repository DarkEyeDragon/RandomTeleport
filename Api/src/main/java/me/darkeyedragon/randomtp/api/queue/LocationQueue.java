package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.event.RandomEvent;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

public class LocationQueue extends ObservableQueue<RandomLocation> {

    private final LocationSearcher baseLocationSearcher;
    public LocationQueue(int capacity, LocationSearcher baseLocationSearcher) {
        super(capacity);
        this.baseLocationSearcher = baseLocationSearcher;
    }

    /*@Override
    public boolean offer(RandomLocation location) {
        //Trigger the event here
        return super.offer(location);
    }*/

    /*@Override
    public RandomLocation poll() {
        return super.poll();
    }*/

    public void generate(SectionWorldDetail sectionWorldDetail) {
        generate(sectionWorldDetail, super.remainingCapacity());
    }
    public void generate(SectionWorldDetail sectionWorldDetail, int amount){
        for(int i = 0; i < amount; i++){
            baseLocationSearcher.getRandom(sectionWorldDetail).thenAccept(super::offer);
        }
    }
}
