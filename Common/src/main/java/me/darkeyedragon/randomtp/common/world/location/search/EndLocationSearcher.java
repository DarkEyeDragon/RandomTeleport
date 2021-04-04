package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;

public class EndLocationSearcher extends BaseLocationSearcher {

    protected final int MIN_DISTANCE = 150;
    protected final int MAX_DISTANCE = 150;

    /**
     * The End location searcher
     *
     * @param plugin The plugin instance
     */
    public EndLocationSearcher(RandomTeleportPlugin<?> plugin) {
        super(plugin.getAddonManager().getAddons(), plugin.getConfigHandler().getSectionBlacklist().getBlacklist(), Dimension.END);
    }

    /*@Override
    public boolean isSafeChunk(RandomChunkSnapshot chunk) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                RandomBlock block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                RandomBiome randomBiome = chunk.getBiome(x, block.getLocation().getBlockY(), z);
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
