package me.darkeyedragon.randomtp.api.world.location.search;

import me.darkeyedragon.randomtp.api.RandomPlugin;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseLocationSearcher implements LocationSearcher {
    protected static final String OCEAN = "ocean";
    protected Set<RandomMaterial> blacklistMaterial;
    protected Set<RandomBiome> blacklistBiome;
    protected final RandomPlugin plugin;

    protected final byte CHUNK_SIZE = 16; //The size (in blocks) of a chunk in all directions
    protected final byte CHUNK_SHIFT = 4; //The amount of bits needed to translate between locations and chunks

    public BaseLocationSearcher(RandomPlugin plugin) {
        //Illegal material types
        blacklistMaterial = new HashSet<>();
        blacklistBiome = new HashSet<>();
        this.plugin = plugin;
    }

    /* This is the final method that will be called from the other end, to get a location */
    @Override
    public CompletableFuture<RandomLocation> getRandom(SectionWorldDetail sectionWorldDetail) {
        CompletableFuture<RandomLocation> location = pickRandomLocation(sectionWorldDetail);
        return location.thenCompose((loc) -> {
            if (loc == null) {
                return getRandom(sectionWorldDetail);
            } else {
                return CompletableFuture.completedFuture(loc);
            }
        });
    }

    /*Pick a random location based on chunks*/
    private CompletableFuture<RandomLocation> pickRandomLocation(SectionWorldDetail sectionWorldDetail) {
        CompletableFuture<RandomChunk> chunk = getRandomChunk(sectionWorldDetail);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    public RandomLocation getRandomLocationFromChunk(RandomChunk chunk) {
        for (int x = 8; x < CHUNK_SIZE; x++) {
            for (int z = 8; z < CHUNK_SIZE; z++) {
                RandomBlock block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                if (isSafe(block.getLocation())) {
                    return block.getLocation();
                }
            }
        }
        return null;
    }

    CompletableFuture<RandomChunk> getRandomChunk(SectionWorldDetail sectionWorldDetail) {
        CompletableFuture<RandomChunk> chunkFuture = getRandomChunkAsync(sectionWorldDetail);
        return chunkFuture.thenCompose((chunk) -> {
            boolean isSafe = isSafeChunk(chunk);
            if (!isSafe) {
                return getRandomChunk(sectionWorldDetail);
            } else return CompletableFuture.completedFuture(chunk);
        });
    }

    CompletableFuture<RandomChunk> getRandomChunkAsync(SectionWorldDetail sectionWorldDetail) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int chunkRadius = sectionWorldDetail.getRadius() >> CHUNK_SHIFT;
        int chunkOffsetX = sectionWorldDetail.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = sectionWorldDetail.getZ() >> CHUNK_SHIFT;
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
        if (block.getType().isAir()) return false;
        if (blacklistMaterial.contains(loc.getBlock().getType())) {
            return false;
        }
        //TODO FIX
        if (block.isPassable()) return false;
        if (block.isLiquid()) return false;
        if (!isSafeAbove(loc)) return false;
        if (!isSafeForPlugins(loc)) return false;
        if (!isSafeSurrounding(loc)) return false;
        return true;
    }

    @Override
    public boolean isSafeForPlugins(RandomLocation location) {
        for (PluginLocationValidator validator : plugin.getValidatorList()) {
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

    public boolean isSafeChunk(RandomChunk chunk) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                RandomBlock block = chunk.getWorld().getHighestBlockAt((chunk.getX() << CHUNK_SHIFT) + x, (chunk.getZ() << CHUNK_SHIFT) + z);
                if (blacklistBiome.contains(block.getBiome())) {
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
            if (!relativeBlock.getType().isSolid()) return false;
            if (blacklistMaterial.contains(relativeBlock.getType())) return false;
        }
        return true;
    }
}
