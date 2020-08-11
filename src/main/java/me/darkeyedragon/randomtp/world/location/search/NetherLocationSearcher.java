package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.validator.ChunkValidator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    @Override
    public boolean isSafe(Location loc) {
        final World world = loc.getWorld();
        if (world == null) return false;
        if (loc.getBlock().getType() == Material.AIR) return false;
        if (blacklistMaterial.contains(loc.getBlock().getType())) return false;
        final Location clone = loc.clone();
        if (clone.add(0, 1, 0).getBlock().getType() != Material.AIR) return false;
        if (clone.add(0, 1, 0).getBlock().getType() != Material.AIR) return false;
        for (ChunkValidator validator : super.getPlugin().getValidatorList()) {
            if (!validator.isValid(loc)) {
                return false;
            }
        }
        return true;
    }
}
