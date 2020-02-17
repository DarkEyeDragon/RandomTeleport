package me.darkeyedragon.randomtp.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LocationHelper {

    private static final String OCEAN = "ocean";
    private final Set<Material> blacklistMaterial;
    private final Set<Biome> blacklistBiome;

    /**
     * A simple utility class to help with {@link Location}
     */
    public LocationHelper() {
        blacklistMaterial = new HashSet<>();
        blacklistBiome = new HashSet<>();
        //Illegal material types
        blacklistMaterial.add(Material.LAVA);
        blacklistMaterial.add(Material.CACTUS);
        blacklistMaterial.add(Material.FIRE);
        blacklistMaterial.add(Material.TRIPWIRE);
        blacklistMaterial.add(Material.ACACIA_PRESSURE_PLATE);
        blacklistMaterial.add(Material.BIRCH_PRESSURE_PLATE);
        blacklistMaterial.add(Material.JUNGLE_PRESSURE_PLATE);
        blacklistMaterial.add(Material.OAK_PRESSURE_PLATE);
        blacklistMaterial.add(Material.SPRUCE_PRESSURE_PLATE);
        blacklistMaterial.add(Material.STONE_PRESSURE_PLATE);
        blacklistMaterial.add(Material.DARK_OAK_PRESSURE_PLATE);
        blacklistMaterial.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        blacklistMaterial.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);

        //Illegal biomes
        for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(biome);
            }
        }
    }

    /**
     *
     * @param world The {@link World} the player will be teleported to.
     * @param radius the max distance from 0, 0 to select a {@link Location} from.
     * @return a random {@link Location}.
     */
    public Location pickRandom(World world, int radius) {
        System.out.println(LocalDateTime.now());
        Random rnd = ThreadLocalRandom.current();
        int x = rnd.nextInt(radius * 2) - radius;
        int z = rnd.nextInt(radius * 2) - radius;

        //Seems to fix some glitches even tho it shouldn't??
        world.getChunkAtAsync(x >> 4, z >> 4).thenAccept(Chunk::load);

        Block block = world.getHighestBlockAt(x, z);
        System.out.println(LocalDateTime.now());
        return block.getLocation().add(0, 1, 0);
    }

    public boolean isSafe(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;
        if (loc.add(0, 2, 0).getBlock().getType() != Material.AIR) return false;
        if (blacklistMaterial.contains(loc.getBlock().getType())) return false;
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return !blacklistBiome.contains(biome);
    }
}
