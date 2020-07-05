package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.World;

public class LocationSearcherFactory {

    public static LocationSearcher getLocationSearcher(World world, RandomTeleport randomTeleport) {
        switch (world.getEnvironment()) {
            case NORMAL:
            case THE_END:
                return new LocationSearcher(randomTeleport);
            case NETHER:
                return new NetherLocationSearcher(randomTeleport);
            default:
                throw new UnsupportedOperationException("This location searcher is not implemented.");
        }
    }
}
