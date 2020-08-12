package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class NetherLocationSearcher extends BaseLocationSearcher {

    private final int MAX_HEIGHT = 120; //Everything above this is nether ceiling

    /**
     * A simple utility class to help with {@link org.bukkit.Location}
     *
     * @param plugin
     */
    public NetherLocationSearcher(RandomTeleport plugin) {
        super(plugin);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    @Override
    public Location getRandomLocationFromChunk(Chunk chunk) {
        for (int x = 8; x < CHUNK_SIZE; x++) {
            for (int z = 8; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < MAX_HEIGHT; y++) {
                    Block block = chunk.getWorld().getBlockAt((chunk.getX() << CHUNK_SHIFT) + x, y, (chunk.getZ() << CHUNK_SHIFT) + z);
                    if (isSafe(block.getLocation())) {
                        return block.getLocation();
                    }
                }
            }
        }
        return null;
    }
}
