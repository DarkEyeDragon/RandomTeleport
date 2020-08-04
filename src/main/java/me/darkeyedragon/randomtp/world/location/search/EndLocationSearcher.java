package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class EndLocationSearcher extends BaseLocationSearcher {

    private final int MIN_DISTANCE = 150;
    private final int MAX_DISTANCE = 150;

    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin The plugin instance
     */
    public EndLocationSearcher(RandomTeleport plugin) {
        super(plugin);
        //setBlacklistBiome(EnumSet.of(Biome.THE_END, Biome.END_BARRENS));
    }

    @Override
    boolean isSafeChunk(Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Block block = chunk.getWorld().getHighestBlockAt((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
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
    }


}
