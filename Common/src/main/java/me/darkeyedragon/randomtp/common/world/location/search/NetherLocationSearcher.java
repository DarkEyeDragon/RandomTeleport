package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;

public class NetherLocationSearcher extends BaseLocationSearcher {

    private final int MAX_HEIGHT = 120; //Everything above this is nether ceiling

    /**
     * @param plugin the {@link RandomTeleportPlugin} instance
     */
    public NetherLocationSearcher(RandomTeleportPlugin<?> plugin) {
        super(plugin.getAddonManager().getAddons(), plugin.getConfigHandler().getSectionBlacklist().getBlacklist(), Dimension.NETHER);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    @Override
    public RandomLocation getRandomLocationFromChunk(RandomChunkSnapshot chunk) {
        for (int x = 2; x < CHUNK_SIZE - 2; x++) {
            for (int z = 2; z < CHUNK_SIZE - 2; z++) {
                for (int y = 0; y < MAX_HEIGHT; y++) {
                    int xLoc = (chunk.getX() << CHUNK_SHIFT) + x;
                    int zLoc = (chunk.getZ() << CHUNK_SHIFT) + z;
                    RandomLocation randomLocation = new CommonLocation(chunk.getWorld(), xLoc, y, zLoc);
                    if (isSafe(randomLocation)) {
                        return randomLocation;
                    }
                }

            }
        }
        return null;
    }
}
