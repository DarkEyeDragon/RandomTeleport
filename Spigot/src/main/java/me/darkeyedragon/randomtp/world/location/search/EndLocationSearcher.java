package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.RandomPlugin;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPlugin;
import org.bukkit.Location;

public class EndLocationSearcher extends BaseLocationSearcher {

    protected final int MIN_DISTANCE = 150;
    protected final int MAX_DISTANCE = 150;

    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin The plugin instance
     */
    public EndLocationSearcher(RandomTeleportPlugin plugin) {
        super(plugin, plugin.getConfigHandler().getConfigBlacklist().getBlacklist(), Dimension.END);
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
