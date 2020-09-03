package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

import java.util.HashMap;
import java.util.Map;

public class LocationSearcherFactory {

    private static final Map<RandomWorld, LocationSearcher> locationSearcherMap = new HashMap<>();

    public static LocationSearcher getLocationSearcher(RandomWorld world, RandomTeleport randomTeleport) {
        switch (world.getEnvironment()) {
            case NORMAL:
                return locationSearcherMap.computeIfAbsent(world, ls -> new OverworldLocationSearcher(randomTeleport));
            case THE_END:
                return locationSearcherMap.computeIfAbsent(world, ls -> new EndLocationSearcher(randomTeleport));
            case NETHER:
                return locationSearcherMap.computeIfAbsent(world, ls -> new NetherLocationSearcher(randomTeleport));
            default:
                throw new UnsupportedOperationException("This location searcher is not implemented.");
        }
    }
}
