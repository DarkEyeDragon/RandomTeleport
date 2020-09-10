package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Material;

public class NetherLocationSearcher extends BaseLocationSearcher<Material> {

    private final int MAX_HEIGHT = 120; //Everything above this is nether ceiling

    /**
     * A simple utility class to help with {@link org.bukkit.Location}
     *
     * @param plugin the plugin instance
     */
    public NetherLocationSearcher(RandomTeleport plugin) {
        super(plugin);
        blacklistMaterial.add(new SpigotMaterial(Material.LAVA));
        blacklistMaterial.add(new SpigotMaterial(Material.CACTUS));
        blacklistMaterial.add(new SpigotMaterial(Material.FIRE));
        blacklistMaterial.add(new SpigotMaterial(Material.MAGMA_BLOCK));
        blacklistMaterial.add(new SpigotMaterial(Material.TRIPWIRE));
        blacklistMaterial.add(new SpigotMaterial(Material.ACACIA_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.BIRCH_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.JUNGLE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.OAK_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.SPRUCE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.STONE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.DARK_OAK_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    @Override
    public RandomLocation getRandomLocationFromChunk(RandomChunk chunk) {
        for (int x = 8; x < CHUNK_SIZE; x++) {
            for (int z = 8; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < MAX_HEIGHT; y++) {
                    RandomBlock block = chunk.getWorld().getBlockAt((chunk.getX() << CHUNK_SHIFT) + x, y, (chunk.getZ() << CHUNK_SHIFT) + z);
                    if (isSafe(block.getLocation())) {
                        return block.getLocation();
                    }
                }
            }
        }
        return null;
    }
}
