package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Location;
import org.bukkit.Material;

public class EndLocationSearcher extends BaseLocationSearcher {

    protected final int MIN_DISTANCE = 150;
    protected final int MAX_DISTANCE = 150;

    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin The plugin instance
     */
    public EndLocationSearcher(RandomTeleport plugin) {
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
