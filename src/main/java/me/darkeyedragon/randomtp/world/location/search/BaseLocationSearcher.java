package me.darkeyedragon.randomtp.world.location.search;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.validator.ChunkValidator;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

abstract class BaseLocationSearcher implements LocationSearcher {

    protected static final String OCEAN = "ocean";
    protected final EnumSet<Material> blacklistMaterial;
    protected final EnumSet<Biome> blacklistBiome;
    protected final RandomTeleport plugin;
    private boolean useWorldBorder;

    protected final byte CHUNK_SIZE = 16; //The size (in blocks) of a chunk in all directions
    protected final byte CHUNK_SHIFT = 4; //The amount of bits needed to translate between locations and chunks

    /**
     * A simple utility class to help with {@link Location}
     */
    public BaseLocationSearcher(RandomTeleport plugin) {
        this.plugin = plugin;
        //Illegal material types
        blacklistMaterial = EnumSet.of(
                Material.LAVA,
                Material.CACTUS,
                Material.FIRE,
                Material.TRIPWIRE,
                Material.ACACIA_PRESSURE_PLATE,
                Material.BIRCH_PRESSURE_PLATE,
                Material.JUNGLE_PRESSURE_PLATE,
                Material.OAK_PRESSURE_PLATE,
                Material.SPRUCE_PRESSURE_PLATE,
                Material.STONE_PRESSURE_PLATE,
                Material.DARK_OAK_PRESSURE_PLATE,
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE
        );
        blacklistBiome = EnumSet.noneOf(Biome.class);

        //Illegal biomes
        for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(biome);
            }
        }
    }

    /* This is the final method that will be called from the other end, to get a location */
    public CompletableFuture<Location> getRandom(WorldConfigSection worldConfigSection) {
        CompletableFuture<Location> location = pickRandomLocation(worldConfigSection);
        return location.thenCompose((loc) -> {
            if (loc == null) {
                return getRandom(worldConfigSection);
            } else return CompletableFuture.completedFuture(loc);
        });
    }

    /*Pick a random location based on chunks*/
    private CompletableFuture<Location> pickRandomLocation(WorldConfigSection worldConfigSection) {
        CompletableFuture<Chunk> chunk = getRandomChunk(worldConfigSection);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    public Location getRandomLocationFromChunk(Chunk chunk) {
        for (int x = 8; x < CHUNK_SIZE; x++) {
            for (int z = 8; z < CHUNK_SIZE; z++) {
                Block block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                if (isSafe(block.getLocation())) {
                    return block.getLocation();
                }
            }
        }
        return null;
    }

    CompletableFuture<Chunk> getRandomChunk(WorldConfigSection worldConfigSection) {
        CompletableFuture<Chunk> chunkFuture = getRandomChunkAsync(worldConfigSection);
        return chunkFuture.thenCompose((chunk) -> {
            boolean isSafe = isSafeChunk(chunk);
            if (!isSafe) {
                return getRandomChunk(worldConfigSection);
            } else return CompletableFuture.completedFuture(chunk);
        });
    }

    CompletableFuture<Chunk> getRandomChunkAsync(WorldConfigSection worldConfigSection) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int chunkRadius = worldConfigSection.getRadius() >> CHUNK_SHIFT;
        int chunkOffsetX = worldConfigSection.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = worldConfigSection.getZ() >> CHUNK_SHIFT;
        int x = rnd.nextInt(-chunkRadius, chunkRadius);
        int z = rnd.nextInt(-chunkRadius, chunkRadius);
        if (PaperLib.isPaper()) {
            return worldConfigSection.getWorld().getChunkAtAsyncUrgently(x + chunkOffsetX, z + chunkOffsetZ);
        }
        return PaperLib.getChunkAtAsync(worldConfigSection.getWorld(), x + chunkOffsetX, z + chunkOffsetZ);
    }

    public boolean isSafe(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;
        if (loc.getBlock().getType().isAir()) return false;
        //Check 2 blocks to see if its safe for the player to stand. Since getHighestBlockAt doesnt include trees
        if (!isSafeAbove(loc)) return false;
        if (blacklistMaterial.contains(loc.getBlock().getType())) return false;
        return isSafeForPlugins(loc);
    }

    protected boolean isSafeForPlugins(Location location) {
        for (ChunkValidator validator : plugin.getValidatorList()) {
            if (!validator.isValid(location)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param location the {@link Location} to validate
     * @return true if the 2 blocks above are air
     */
    protected boolean isSafeAbove(Location location) {
        Block blockAbove = location.getBlock().getRelative(BlockFace.UP);
        Block blockAboveAbove = blockAbove.getRelative(BlockFace.UP);
        return (blockAbove.isPassable() && blockAboveAbove.isPassable());
    }

    public boolean isSafeChunk(Chunk chunk) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                Block block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                if (blacklistBiome.contains(block.getBiome())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isUseWorldBorder() {
        return useWorldBorder;
    }

    public void setUseWorldBorder(boolean useWorldBorder) {
        this.useWorldBorder = useWorldBorder;
    }

    public RandomTeleport getPlugin() {
        return plugin;
    }
}
