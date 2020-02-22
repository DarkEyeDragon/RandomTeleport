package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.validator.ChunkValidator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class LocationHelper {

    private static final String OCEAN = "ocean";
    private final Set<Material> blacklistMaterial;
    private final Set<Biome> blacklistBiome;
    private Location lastLocaction;
    private RandomTeleport plugin;

    /**
     * A simple utility class to help with {@link Location}
     */
    public LocationHelper(RandomTeleport plugin) {
        this.plugin = plugin;
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

    public CompletableFuture<Location> getRandomLocation(World world, int radius) {
        CompletableFuture<Location> location = pickRandomLocation(world, radius);
        return location.thenCompose((loc) -> {
            if (loc == null) {
                return getRandomLocation(world, radius);
            } else return CompletableFuture.completedFuture(loc);
        });
    }

    /* This is the final method that will be called from the other end, to get a location */
    private CompletableFuture<Location> pickRandomLocation(World world, int radius) {
        CompletableFuture<Chunk> chunk = getRandomChunk(world, radius);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    private Location getRandomLocationFromChunk(Chunk chunk) {
        for (int i = 8; i < 16; i++) {
            for (int j = 8; j < 16; j++) {
                Block block = chunk.getBlock(i, 64, j);
                if (isSafeLocation(block.getLocation())) {
                    return block.getLocation();
                }
            }
        }

        return null;
    }

    private CompletableFuture<Chunk> getRandomChunk(World world, int radius) {
        var chunkFuture = getRandomChunkAsync(world, radius/16);

        return chunkFuture.thenCompose((chunk) -> {
            boolean isSafe = isSafeChunk(chunk);
            if (!isSafe) {
                return getRandomChunk(world, radius);
            }
            else return CompletableFuture.completedFuture(chunk);
        });
    }
    private CompletableFuture<Chunk> getRandomChunkAsync(World world, int radius) {
        Random rnd = ThreadLocalRandom.current();
        int x = rnd.nextInt(radius * 2) - radius;
        int z = rnd.nextInt(radius * 2) - radius;
        return world.getChunkAtAsync(x, z);
    }

    public boolean isSafeLocation(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;
        if (loc.add(0, 2, 0).getBlock().getType() != Material.AIR) return false;
        if(blacklistMaterial.contains(loc.getBlock().getType())) return false;
        for (ChunkValidator validator : plugin.getValidatorList()) {
            if(!validator.isValid(loc)){
                return false;
            }
        }
        return true;
    }

    private boolean isSafeChunk(Chunk chunk) {

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (!blacklistBiome.contains(chunk.getBlock(i, 64, j).getBiome())) {
                    return true;
                }
            }
        }
        return false;
    }
}
