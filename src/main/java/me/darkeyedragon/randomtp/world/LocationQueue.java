package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import me.darkeyedragon.randomtp.world.location.search.BaseLocationSearcher;
import org.bukkit.Location;

public class LocationQueue extends ObservableQueue<Location> {
    private final BaseLocationSearcher baseLocationSearcher;

    public LocationQueue(int capacity, BaseLocationSearcher baseLocationSearcher) {
        super(capacity);
        this.baseLocationSearcher = baseLocationSearcher;
    }

    public boolean offer(Location location) {
        return super.offer(location);
    }

    public Location poll() {
        return super.poll();
    }

    public void generate(WorldConfigSection worldConfigSection) {
        generate(worldConfigSection, super.remainingCapacity());
    }
    public void generate(WorldConfigSection worldConfigSection, int amount){
        for(int i = 0; i < amount; i++){
            baseLocationSearcher.getRandom(worldConfigSection).thenAccept(this::offer);
        }
    }
}
