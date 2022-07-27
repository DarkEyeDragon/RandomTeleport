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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

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

    private static final Component PASS = Component.text("PASS").color(TextColor.color(0x00ff00));
    private static final Component FAIL = Component.text("FAIL").color(TextColor.color(0xff0000));

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
        plugin.getMessageHandler().sendDebugMessage("2. Looking for location from chunk...");
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
        plugin.getMessageHandler().sendDebugMessage("No safe location found in chunk: {x:" + chunk.getX() + " z:" + chunk.getZ() + " world: " + chunk.getWorld() + "}");
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
                            plugin.getMessageHandler().sendDebugMessage("1. Found safe chunk...");
                            return CompletableFuture.completedFuture(snapshot);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                plugin.getMessageHandler().sendDebugMessage("1. Not a safe chunk...");
                return CompletableFuture.completedFuture(null);
            } else {
                plugin.getMessageHandler().sendDebugMessage("1. Not a safe chunk...");
                return CompletableFuture.completedFuture(chunk);
            }
        });
    }

    CompletableFuture<RandomChunkSnapshot> getRandomChunkAsync(LocationDataProvider dataProvider) {
        plugin.getMessageHandler().sendDebugMessage("1. Getting random chunk async...");
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        RandomOffset offset = dataProvider.getOffset();
        int radius = dataProvider.getRadius();
        int chunkRadius = radius >> CHUNK_SHIFT;
        int chunkOffsetX = offset.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = offset.getZ() >> CHUNK_SHIFT;
        int x = rnd.nextInt(-chunkRadius, chunkRadius + 1);
        int z = rnd.nextInt(-chunkRadius, chunkRadius + 1);
        RandomWorld world = dataProvider.getWorld();
        if (world == null) {
            plugin.getMessageHandler().sendDebugMessage("1.1 World is null...");
            return CompletableFuture.completedFuture(null);
        }
        plugin.getMessageHandler().sendDebugMessage("1.1 Found random chunk in \"" + world.getName() + "\"");
        return world.getChunkAtAsync(world, x + chunkOffsetX, z + chunkOffsetZ);
    }

    @Override
    public boolean isSafe(RandomLocation loc) {
        plugin.getMessageHandler().sendDebugMessage("3. Checking if safe...");
        Component worldDebug = Component.text("3.1 world not null? ");
        RandomWorld world = loc.getWorld();
        if (world == null) {
            plugin.getMessageHandler().sendDebugMessage(worldDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(worldDebug.append(PASS));
        RandomBlock block = loc.getBlock();
        RandomBlockType blockType = block.getBlockType();
        Component airDebug = Component.text("3.2 block not air? ");
        if (blockType.getType().isAir()) {
            plugin.getMessageHandler().sendDebugMessage(airDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(airDebug.append(PASS));
        //Check if it passes the global blacklist
        Component blacklistDebug = Component.text("3.3 block not on global blacklist? ");
        if (!isValidGlobalBlockType(loc)) {
            plugin.getMessageHandler().sendDebugMessage(blacklistDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(blacklistDebug.append(PASS));
        RandomDimensionData dimensionData = blacklist.getDimensionData(dimension);
        //Check if it passes the dimension blacklist
        Component dimDebug = Component.text("3.4 block not on dimension blacklist? ");
        if (dimensionData.getBlockTypes().contains(blockType)) {
            plugin.getMessageHandler().sendDebugMessage(dimDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(dimDebug.append(PASS));
        Component passableDebug = Component.text("3.5 block not passable? ");
        if (block.isPassable()) {
            plugin.getMessageHandler().sendDebugMessage(passableDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(passableDebug.append(PASS));
        Component liquidDebug = Component.text("3.6 block not a liquid? ");
        if (block.isLiquid()) {
            plugin.getMessageHandler().sendDebugMessage(liquidDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(liquidDebug.append(PASS));

        Component safeAboveDebug = Component.text("3.7 block above safe? ");
        if (!isSafeAbove(loc)) {
            plugin.getMessageHandler().sendDebugMessage(safeAboveDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(safeAboveDebug.append(PASS));

        Component pluginsDebug = Component.text("3.8 block is safe for addons? ");
        if (!isSafeForPlugins(loc)) {
            plugin.getMessageHandler().sendDebugMessage(pluginsDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(pluginsDebug.append(PASS));

        Component surDebug = Component.text("3.9 Block surroundings safe? ");
        if (!isSafeSurrounding(loc)) {
            plugin.getMessageHandler().sendDebugMessage(surDebug.append(FAIL));
            return false;
        }
        plugin.getMessageHandler().sendDebugMessage(surDebug.append(PASS));
        plugin.getMessageHandler().sendDebugMessage("3.10 Safe location found: " + loc);
        return true;
    }

    @Override
    public boolean isSafeForPlugins(RandomLocation location) {
        for (RandomLocationValidator validator : validatorMap.values()) {
            if (!validator.isValid(location)) {
                plugin.getMessageHandler().sendDebugMessage("3.1 Deemed unsafe for addon: " + validator.getIdentifier());
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
        for (int x = 0; x < CHUNK_SIZE; x += 4) {
            for (int z = 0; z < CHUNK_SIZE; z += 4) {
                int y = chunk.getHighestBlockYAt(x, z);
                RandomBiome randomBiome = chunk.getBiome(x, y, z);
                if (isBlacklistedBiome(randomBiome)) {
                    plugin.getMessageHandler().sendDebugMessage("1.3 Biome at x" + x + "y " + y + "z" + z + " is a blacklisted biome");
                    return false;
                }
            }
        }
        plugin.getMessageHandler().sendDebugMessage("1.3 Chunk is safe...");
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
