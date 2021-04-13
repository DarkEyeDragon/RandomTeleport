package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;

public class OverworldLocationSearcher extends BaseLocationSearcher {
    /**
     * Overworld location implementation of {@link BaseLocationSearcher}
     *
     * @param plugin the {@link RandomTeleportPlugin} instance
     */
    public OverworldLocationSearcher(RandomTeleportPlugin<?> plugin) {
        super(plugin, plugin.getAddonManager().getAddons(), plugin.getConfigHandler().getSectionBlacklist().getBlacklist(), Dimension.OVERWORLD);

        //TODO parse regex
        //Illegal biomes
        /*for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(new SpigotBiome(biome));
            }
        }*/
    }
}
