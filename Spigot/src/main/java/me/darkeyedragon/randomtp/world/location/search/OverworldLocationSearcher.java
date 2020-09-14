package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import org.bukkit.Location;

public class OverworldLocationSearcher extends BaseLocationSearcher {
    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin the {@link RandomTeleport} instance
     */
    public OverworldLocationSearcher(RandomTeleport plugin) {
        super(plugin, plugin.getConfigHandler().getConfigBlacklist().getBlacklist(), Dimension.OVERWORLD);

        //TODO parse regex
        //Illegal biomes
        /*for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(new SpigotBiome(biome));
            }
        }*/
    }
}
