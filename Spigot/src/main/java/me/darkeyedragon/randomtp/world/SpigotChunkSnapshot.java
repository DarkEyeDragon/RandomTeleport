package me.darkeyedragon.randomtp.world;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;

public class SpigotChunkSnapshot implements RandomChunkSnapshot {

    private final ChunkSnapshot chunk;
    private final boolean useNew;

    public SpigotChunkSnapshot(ChunkSnapshot chunk) {
        useNew = PaperLib.isVersion(18, 2);
        this.chunk = chunk;
    }

    @Override
    public RandomWorld getWorld() {
        return new SpigotWorld(Bukkit.getWorld(chunk.getWorldName()));
    }

    @Override
    public int getX() {
        return chunk.getX();
    }

    @Override
    public int getZ() {
        return chunk.getZ();
    }

    /**
     * Gets the highest non-air coordinate at the given coordinates.
     *
     * @param x X-coordinate of the blocks (0-15)
     * @param z Z-coordinate of the blocks (0-15)
     * @return Y-coordinate of the highest non-air block
     */
    public int getHighestBlockYAt(int x, int z) {
        //https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/ce373be660c95d5ca2b2d167545565f9636f9cd8
        if (useNew) {
            return Math.max(chunk.getHighestBlockYAt(x, z), 0);
        }
        return Math.max(chunk.getHighestBlockYAt(x, z) - 1, 0);
    }

    @Override
    public RandomBiome getBiome(int x, int y, int z) {
        return WorldUtil.toRandomBiome(chunk.getBiome(x, y, z));
    }
}
