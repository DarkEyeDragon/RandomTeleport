package me.darkeyedragon.randomtp.sponge.world;

import com.flowpowered.math.vector.Vector3i;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.CommonChunkSnapshot;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;

public class SpongeChunk extends CommonChunkSnapshot {
    private final Chunk chunk;
    private final Location<Chunk> location;

    public SpongeChunk(Location<Chunk> location) {
        this.location = location;
        chunk = location.getExtent();
    }

    @Override
    public RandomWorld getWorld() {
        return new SpongeWorld(chunk.getWorld());
    }

    @Override
    public int getX() {
        return location.getChunkPosition().getX();
    }

    @Override
    public int getZ() {
        return location.getChunkPosition().getZ();
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return chunk.getHighestYAt(x, z);
    }

    @Override
    public RandomBiome getBiome(int x, int y, int z) {
        Vector3i biomeMin = chunk.getBiomeMin();
        return new SpongeBiome(chunk.getBiome(biomeMin.getX() + x, biomeMin.getY() + y, biomeMin.getZ() + z));
    }
}
