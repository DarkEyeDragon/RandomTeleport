package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.World;

public class LocationSearcherFactory {

    public static BaseLocationSearcher getLocationSearcher(World world, RandomTeleport randomTeleport) {
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
