package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.bukkit.Location;

public class OverworldLocationSearcher extends BaseLocationSearcher {
    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin the {@link RandomTeleportPlugin} instance
     */
    public OverworldLocationSearcher(RandomTeleportPlugin<RandomTeleportPluginImpl> plugin) {
        super(plugin.getInstance().getValidatorSet(), plugin.getConfigHandler().getSectionBlacklist().getBlacklist(), Dimension.OVERWORLD);

        //TODO parse regex
        //Illegal biomes
        /*for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(new SpigotBiome(biome));
            }
        }*/
    }
}
