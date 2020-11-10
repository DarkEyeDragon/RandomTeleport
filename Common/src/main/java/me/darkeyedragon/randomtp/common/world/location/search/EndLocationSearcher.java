package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;

public class EndLocationSearcher extends BaseLocationSearcher {

    protected final int MIN_DISTANCE = 150;
    protected final int MAX_DISTANCE = 150;

    /**
     * The End location searcher
     *
     * @param plugin The plugin instance
     */
    public EndLocationSearcher(RandomTeleportPlugin<RandomTeleportPluginImpl> plugin) {
        super(plugin.getInstance().getAddonManager().getAddons(), plugin.getConfigHandler().getSectionBlacklist().getBlacklist(), Dimension.END);
    }

    /*@Override
    public boolean isSafeChunk(RandomChunk chunk) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                RandomBlock block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                if (block.getBiome() == Biome.THE_END) {
                    if ((Math.abs(block.getX()) > MIN_DISTANCE || Math.abs(block.getZ()) > MIN_DISTANCE) && (Math.abs(block.getX()) < MAX_DISTANCE || Math.abs(block.getZ()) < MAX_DISTANCE)) {
                        return false;
                    }
                }
                if (blacklistBiome.contains(block.getBiome())) {
                    return false;
                }
            }
        }
        return true;
    }*/
}
