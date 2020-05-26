package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.location.LocationSearcher;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import org.bukkit.Location;

public class LocationQueue extends ObservableQueue<Location> {
    private final LocationSearcher locationSearcher;

    public LocationQueue(int capacity, LocationSearcher locationSearcher) {
        super(capacity);
        this.locationSearcher = locationSearcher;
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
            locationSearcher.getRandomLocation(worldConfigSection).thenAccept(this::offer);
        }
    }
}
