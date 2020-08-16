package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import org.bukkit.Location;
import org.bukkit.block.Biome;

public class OverworldLocationSearcher extends BaseLocationSearcher<Biome> {
    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin the {@link RandomTeleport} instance
     */
    public OverworldLocationSearcher(RandomTeleport plugin) {
        super(plugin);
    }
}
