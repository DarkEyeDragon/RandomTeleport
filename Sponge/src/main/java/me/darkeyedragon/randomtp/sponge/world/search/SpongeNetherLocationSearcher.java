package me.darkeyedragon.randomtp.sponge.world.search;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.common.world.location.search.NetherLocationSearcher;

public class SpongeNetherLocationSearcher extends NetherLocationSearcher {
    /**
     * @param plugin the {@link RandomTeleportPlugin} instance
     */
    public SpongeNetherLocationSearcher(RandomTeleportPlugin<?> plugin) {
        super(plugin);
    }

    @Override
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
