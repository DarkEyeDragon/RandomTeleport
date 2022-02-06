package me.darkeyedragon.randomtp.sponge.world.search;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.common.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.common.world.location.search.OverworldLocationSearcher;

public class SpongeOverworldLocationSearcher extends OverworldLocationSearcher {

    /**
     * Overworld location implementation of {@link BaseLocationSearcher}
     *
     * @param plugin the {@link RandomTeleportPlugin} instance
     */
    public SpongeOverworldLocationSearcher(RandomTeleportPlugin<?> plugin) {
        super(plugin);
    }

    public boolean isSafeChunk(RandomChunkSnapshot chunk) {
        for (int x = 2; x < CHUNK_SIZE - 2; x++) {
            for (int z = 2; z < CHUNK_SIZE - 2; z++) {
                RandomBiome randomBiome = chunk.getBiome(x, 0, z);
                if (isBlacklistedBiome(randomBiome)) {
                    return false;
                }
            }
        }
        return true;
    }

}
