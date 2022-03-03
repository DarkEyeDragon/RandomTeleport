package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;
import me.darkeyedragon.randomtp.api.world.location.search.LocationDataProvider;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.util.ChunkTraverser;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseLocationSearcher implements LocationSearcher {

    protected final Map<String, ? extends RandomLocationValidator> validatorMap;
    private final Dimension dimension;
    private final RandomTeleportPlugin<?> plugin;
    private final RandomBlacklist blacklist;

    protected final byte CHUNK_SIZE = 16; //The size (in blocks) of a chunk in all directions
    protected final byte CHUNK_SHIFT = 4; //The amount of bits needed to translate between locations and chunks
    protected int count = 1;
    protected int max = 50;

    public BaseLocationSearcher(RandomTeleportPlugin<?> plugin, Map<String, ? extends RandomLocationValidator> validatorMap, RandomBlacklist blacklist, Dimension dimension) {
        this.plugin = plugin;
        this.blacklist = blacklist;
        this.dimension = dimension;
        this.validatorMap = validatorMap;
    }

    /**
     * This method will search recursively until it reached 50 tries, then fail silently.
     *
     * @param dataProvider the data required to find a random location
     * @return a {@link CompletableFuture<RandomLocation>} holding the location. Null if no location is found.
     */
    @Override
    public CompletableFuture<RandomLocation> getRandom(LocationDataProvider dataProvider) {
        return pickRandomLocation(dataProvider);
    }

    /*Pick a random location based on chunks*/
    private CompletableFuture<RandomLocation> pickRandomLocation(LocationDataProvider dataProvider) {
        CompletableFuture<RandomChunkSnapshot> chunk = getRandomChunk(dataProvider);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    public RandomLocation getRandomLocationFromChunk(RandomChunkSnapshot chunk) {
        for (int x = 2; x < CHUNK_SIZE - 2; x++) {
            for (int z = 2; z < CHUNK_SIZE - 2; z++) {
                //RandomBlock block = chunk.get((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                int xLoc = (chunk.getX() << CHUNK_SHIFT) + x;
                int zLoc = (chunk.getZ() << CHUNK_SHIFT) + z;
                int y = chunk.getHighestBlockYAt(x, z);
                RandomLocation randomLocation = new CommonLocation(chunk.getWorld(), xLoc, y, zLoc);
                if (isSafe(randomLocation)) {
                    return randomLocation;
                }
            }
        }
        return null;
    }

    CompletableFuture<RandomChunkSnapshot> getRandomChunk(LocationDataProvider dataProvider) {
        CompletableFuture<RandomChunkSnapshot> chunkFuture = getRandomChunkAsync(dataProvider);
        return chunkFuture.thenCompose((chunk) -> {
            if (!isSafeChunk(chunk)) {
                ChunkTraverser chunkTraverser = new ChunkTraverser(chunk);
                while (chunkTraverser.hasNext()) {
                    try {
                        RandomChunkSnapshot snapshot = chunkTraverser.next().get();
                        int x = snapshot.getX() << CHUNK_SHIFT;
                        int z = snapshot.getZ() << CHUNK_SHIFT;
                        RandomOffset offset = dataProvider.getOffset();
                        int radius = dataProvider.getRadius();
                        boolean withinBounds = (x < radius + offset.getX() && z < radius + offset.getZ()) || (x > radius - offset.getX() && z > radius - offset.getZ());
                        if (withinBounds && isSafeChunk(snapshot)) {
                            return CompletableFuture.completedFuture(snapshot);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return CompletableFuture.completedFuture(null);
            } else {
                return CompletableFuture.completedFuture(chunk);
            }
        });
    }

    CompletableFuture<RandomChunkSnapshot> getRandomChunkAsync(LocationDataProvider dataProvider) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        RandomOffset offset = dataProvider.getOffset();
        int radius = dataProvider.getRadius();
        int chunkRadius = radius >> CHUNK_SHIFT;
        int chunkOffsetX = offset.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = offset.getZ() >> CHUNK_SHIFT;
        int x = rnd.nextInt(-chunkRadius, chunkRadius + 1);
        int z = rnd.nextInt(-chunkRadius, chunkRadius + 1);
        RandomWorld world = dataProvider.getWorld();
        if (world == null) return CompletableFuture.completedFuture(null);
        return world.getChunkAtAsync(world, x + chunkOffsetX, z + chunkOffsetZ);
    }

    @Override
    public boolean isSafe(RandomLocation loc) {
        RandomWorld world = loc.getWorld();
        if (world == null) return false;
        RandomBlock block = loc.getBlock();
        if (block.getBlockType().getType().isAir()) return false;
        RandomBlockType blockType = loc.getBlock().getBlockType();
        //Check if it passes the global blacklist
        if (!isValidGlobalBlockType(loc)) return false;
        RandomDimensionData dimensionData = blacklist.getDimensionData(dimension);
        //Check if it passes the dimension blacklist
        if (dimensionData.getBlockTypes().contains(blockType)) return false;
        if (block.isPassable()) return false;
        if (block.isLiquid()) return false;
        if (!isSafeAbove(loc)) return false;
        if (!isSafeForPlugins(loc)) return false;
        return isSafeSurrounding(loc);
    }

    @Override
    public boolean isSafeForPlugins(RandomLocation location) {
        for (RandomLocationValidator validator : validatorMap.values()) {
            if (!validator.isValid(location)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param location the {@link RandomLocation} to validate
     * @return true if the 2 blocks above are safe
     */
    protected boolean isSafeAbove(RandomLocation location) {
        RandomBlock blockAbove = location.getBlock().getRelative(BlockFace.UP);
        RandomBlock blockAboveAbove = blockAbove.getRelative(BlockFace.UP);
        return (blockAbove.isPassable() && blockAboveAbove.isPassable() && !blockAbove.isLiquid());
    }

    public boolean isSafeChunk(RandomChunkSnapshot chunk) {
        for (int x = 2; x < CHUNK_SIZE - 2; x++) {
            for (int z = 2; z < CHUNK_SIZE - 2; z++) {
                int y = chunk.getHighestBlockYAt(x, z);
                RandomBiome randomBiome = chunk.getBiome(x, y, z);
                if (isBlacklistedBiome(randomBiome)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param location the {@link RandomLocation} to check around
     * @return true if the surroundings are safe
     */
    protected boolean isSafeSurrounding(RandomLocation location) {
        RandomBlock block = location.getBlock();
        EnumSet<BlockFace> blockfaces = EnumSet.allOf(BlockFace.class);
        blockfaces.remove(BlockFace.UP);
        blockfaces.remove(BlockFace.DOWN);
        for (BlockFace blockFace : blockfaces) {
            RandomBlock relativeBlock = block.getRelative(blockFace);
            boolean isChunkLoaded = location.getWorld().isChunkLoaded(relativeBlock.getLocation().getBlockX() >> CHUNK_SHIFT, relativeBlock.getLocation().getBlockZ() >> CHUNK_SHIFT);
            if (!isChunkLoaded) {
                System.out.println("Unloaded chunk access");
                Thread.dumpStack();
                return false;
            }
            if (relativeBlock.isEmpty()) return false;
            if (!relativeBlock.getBlockType().getType().isSolid()) return false;
            if (blacklist.getDimensionData(dimension).getBlockTypes().contains(relativeBlock.getBlockType()))
                return false;
        }
        return true;
    }

    /**
     * @param location the {@link RandomLocation} to validate
     * @return true if the global blacklist does not contain the {@link RandomBlockType}
     */
    protected boolean isValidGlobalBlockType(RandomLocation location) {
        RandomBlock randomBlock = location.getBlock();
        RandomDimensionData dimensionData = blacklist.getDimensionData(Dimension.GLOBAL);
        return !dimensionData.getBlockTypes().contains(randomBlock.getBlockType());
    }

    protected boolean isBlacklistedBiome(RandomBiome randomBiome) {
        RandomDimensionData globalData = blacklist.getDimensionData(Dimension.GLOBAL);
        RandomDimensionData dimensionData = blacklist.getDimensionData(dimension);
        return globalData.getBiomes().contains(randomBiome) || dimensionData.getBiomes().contains(randomBiome);
    }
}
