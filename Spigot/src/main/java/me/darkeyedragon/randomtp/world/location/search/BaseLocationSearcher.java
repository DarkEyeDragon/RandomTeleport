//package me.darkeyedragon.randomtp.world.location.search;

/*abstract class BaseLocationSearcher implements LocationSearcher<World> {

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
    /*public BaseLocationSearcher(RandomTeleport plugin) {
        this.plugin = plugin;
        //Illegal material types
        blacklistMaterial = EnumSet.of(
                Material.LAVA,
                Material.CACTUS,
                Material.FIRE,
                Material.MAGMA_BLOCK,
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
    /*@Override
    public CompletableFuture<RandomLocation> getRandom(SectionWorldDetail<World> sectionWorldDetail) {
        CompletableFuture<Location> location = pickRandomLocation(sectionWorldDetail);
        return location.thenCompose((loc) -> {
            if (loc == null) {
                return getRandom(sectionWorldDetail);
            } else return CompletableFuture.completedFuture(loc);
        });
    }

    /*Pick a random location based on chunks*/
    /*private CompletableFuture<Location> pickRandomLocation(SectionWorldDetail<World> sectionWorldDetail) {
        CompletableFuture<Chunk> chunk = getRandomChunk(sectionWorldDetail);
        return chunk.thenApply(this::getRandomLocationFromChunk);
    }

    /* Will search through the chunk to find a location that is safe, returning null if none is found. */
    /*public Location getRandomLocationFromChunk(Chunk chunk) {
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

    CompletableFuture<Chunk> getRandomChunk(SectionWorldDetail<World> sectionWorldDetail) {
        CompletableFuture<Chunk> chunkFuture = getRandomChunkAsync(sectionWorldDetail);
        return chunkFuture.thenCompose((chunk) -> {
            boolean isSafe = isSafeChunk(chunk);
            if (!isSafe) {
                return getRandomChunk(sectionWorldDetail);
            } else return CompletableFuture.completedFuture(chunk);
        });
    }

    CompletableFuture<Chunk> getRandomChunkAsync(SectionWorldDetail<World> sectionWorldDetail) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int chunkRadius = sectionWorldDetail.getRadius() >> CHUNK_SHIFT;
        int chunkOffsetX = sectionWorldDetail.getX() >> CHUNK_SHIFT;
        int chunkOffsetZ = sectionWorldDetail.getZ() >> CHUNK_SHIFT;
        int x = rnd.nextInt(-chunkRadius, chunkRadius);
        int z = rnd.nextInt(-chunkRadius, chunkRadius);
        World world = sectionWorldDetail.getWorld();
        if(world == null) return CompletableFuture.completedFuture(null);
        if (PaperLib.isPaper()) {
            return world.getChunkAtAsyncUrgently(x + chunkOffsetX, z + chunkOffsetZ);
        }
        return PaperLib.getChunkAtAsync(world, x + chunkOffsetX, z + chunkOffsetZ);
    }

    @Override
    public boolean isSafe(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;
        if (blacklistMaterial.contains(loc.getBlock().getType())) return false;
        if (!loc.getBlock().getType().isSolid()) return false;
        if (!isSafeAbove(loc)) return false;
        if (!isSafeForPlugins(loc)) return false;
        return isSafeSurrounding(loc);
    }

    @Override
    public boolean isSafeForPlugins(Location location) {
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
    /*protected boolean isSafeAbove(Location location) {
        Block blockAbove = location.getBlock().getRelative(BlockFace.UP);
        Block blockAboveAbove = blockAbove.getRelative(BlockFace.UP);
        return (blockAbove.isPassable() && blockAboveAbove.isPassable() && !blockAbove.isLiquid());
    }

    public boolean isSafeChunk(Chunk chunk) {
        if(chunk == null) return false;
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

    /**
     * @param location the {@link Location} to check around
     * @return true if the surroundings are safe
     */
    /*protected boolean isSafeSurrounding(Location location) {
        Block block = location.getBlock();
        EnumSet<BlockFace> blockFaces = EnumSet.allOf(BlockFace.class);
        blockFaces.remove(BlockFace.UP);
        blockFaces.remove(BlockFace.DOWN);
        for (BlockFace blockFace : blockFaces) {
            Block relativeBlock = block.getRelative(blockFace);
            if (relativeBlock.isEmpty()) return false;
            if (!relativeBlock.getType().isSolid()) return false;
            if (blacklistMaterial.contains(relativeBlock.getType())) return false;
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
}*/
