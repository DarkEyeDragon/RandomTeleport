package me.darkeyedragon.randomtp.api.world.location.search;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.DimensionData;
import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.Offset;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseLocationSearcher implements LocationSearcher {

    protected final Set<PluginLocationValidator> validatorSet;
    private final Dimension dimension;
    private final RandomBlacklist blacklist;

    protected final byte CHUNK_SIZE = 16; //The size (in blocks) of a chunk in all directions
    protected final byte CHUNK_SHIFT = 4; //The amount of bits needed to translate between locations and chunks
    protected int count = 1;
    protected int max = 50;

    public BaseLocationSearcher(Set<PluginLocationValidator> validatorSet, RandomBlacklist blacklist, Dimension dimension) {
        this.blacklist = blacklist;
        this.dimension = dimension;
        this.validatorSet = validatorSet;
    }

    /**
     * This method will search recursively until it reached 50 tries, then fail silently.
     * @param sectionWorldDetail
     * @return a {@link CompletableFuture<RandomLocation>} holding the location. Null if no location is found.
     */
    @Override
    public CompletableFuture<RandomLocation> getRandom(SectionWorldDetail sectionWorldDetail) {
        return pickRandomLocation(sectionWorldDetail).thenCompose((loc) -> {
            if (loc == null) {
                if (count < max) {
                    count++;
                    return getRandom(sectionWorldDetail);
                }
                //TODO handle failed searches properly.
                return null;
            } else {
                loc.setTries(count);
                count = 0;
                return CompletableFuture.completedFuture(loc);
            }
        });
    }

    /*Pick a random location based on chunks*/
    private CompletableFuture<RandomLocation> pickRandomLocation(SectionWorldDetail sectionWorldDetail) {
        CompletableFuture<RandomChunkSnapshot> chunk = getRandomChunk(sectionWorldDetail);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    public RandomLocation getRandomLocationFromChunk(RandomChunkSnapshot chunk) {
        for (int x = 8; x < CHUNK_SIZE; x++) {
            for (int z = 8; z < CHUNK_SIZE; z++) {
                //RandomBlock block = chunk.get((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                int xChunk = (chunk.getX() << CHUNK_SHIFT) + x;
                int zChunk = (chunk.getZ() << CHUNK_SHIFT) + z;
                int y = chunk.getHighestBlockYAt(x, z);
                RandomLocation randomLocation = new RandomLocation(chunk.getWorld(), xChunk, y, zChunk);
                if (isSafe(randomLocation)) {
                    return randomLocation;
                }
            }
        }
        return null;
    }

    CompletableFuture<RandomChunkSnapshot> getRandomChunk(SectionWorldDetail sectionWorldDetail) {
        CompletableFuture<RandomChunkSnapshot> chunkFuture = getRandomChunkAsync(sectionWorldDetail);
        return chunkFuture.thenCompose((chunk) -> {
            boolean isSafe = isSafeChunk(chunk);
            if (!isSafe) {
                return getRandomChunk(sectionWorldDetail);
            } else return CompletableFuture.completedFuture(chunk);
        });
    }

    CompletableFuture<RandomChunkSnapshot> getRandomChunkAsync(SectionWorldDetail sectionWorldDetail) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        Offset offset = sectionWorldDetail.getOffset();
        int chunkRadius = offset.getRadius() >> CHUNK_SHIFT;
        int chunkOffsetX = offset.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = offset.getZ() >> CHUNK_SHIFT;
        int x = rnd.nextInt(-chunkRadius, chunkRadius);
        int z = rnd.nextInt(-chunkRadius, chunkRadius);
        RandomWorld world = sectionWorldDetail.getWorld();
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
        DimensionData dimensionData = blacklist.getDimensionData(dimension);
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
        for (PluginLocationValidator validator : validatorSet) {
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
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int xChunk = chunk.getX() + x;
                int zChunk = chunk.getZ() + z;
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
            if (relativeBlock.isEmpty()) return false;
            if (!relativeBlock.getBlockType().getType().isSolid()) return false;
            if (blacklist.getDimensionData(dimension).getBlockTypes().contains(relativeBlock.getBlockType())) ;
        }
        return true;
    }

    /**
     * @param location the {@link RandomLocation} to validate
     * @return true if the global blacklist does not contain the {@link RandomBlockType}
     */
    protected boolean isValidGlobalBlockType(RandomLocation location) {
        RandomBlock randomBlock = location.getBlock();
        DimensionData dimensionData = blacklist.getDimensionData(Dimension.GLOBAL);
        return !dimensionData.getBlockTypes().contains(randomBlock.getBlockType());
    }

    protected boolean isBlacklistedBiome(RandomBiome randomBiome) {
        DimensionData globalData = blacklist.getDimensionData(Dimension.GLOBAL);
        DimensionData dimensionData = blacklist.getDimensionData(dimension);
        return globalData.getBiomes().contains(randomBiome) || dimensionData.getBiomes().contains(randomBiome);
    }

}
