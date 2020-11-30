package me.darkeyedragon.randomtp.api.world;

public interface RandomChunkSnapshot {

    RandomWorld getWorld();

    int getX();

    int getZ();

    /**
     * Gets the highest non-air coordinate at the given coordinates
     *
     * @param x X-coordinate of the blocks (0-15)
     * @param z Z-coordinate of the blocks (0-15)
     * @return Y-coordinate of the highest non-air block
     */
    int getHighestBlockYAt(int x, int z);

    RandomBiome getBiome(int x, int y, int z);
}
