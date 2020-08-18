package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSearcherFactory {

    public static LocationSearcher getLocationSearcher(RandomWorld world, RandomTeleport randomTeleport) {
        switch (world.getEnvironment()) {
            case NORMAL:
                return new OverworldLocationSearcher(randomTeleport);
            case THE_END:
                return new EndLocationSearcher(randomTeleport);
            case NETHER:
                return new NetherLocationSearcher(randomTeleport);
            default:
                throw new UnsupportedOperationException("This location searcher is not implemented.");
        }
    }
}
