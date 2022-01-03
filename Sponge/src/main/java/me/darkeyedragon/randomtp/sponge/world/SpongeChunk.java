package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SpongeChunk implements RandomChunkSnapshot {
    private final Chunk chunk;
    private final Location<World> location;

    public SpongeChunk(Location<World> location) {
        this.location = location;
        chunk = location.getExtent()
                .getChunk(location.getChunkPosition())
                .orElseThrow(() -> new IllegalArgumentException("No chunk associated with location " + location));
    }

    @Override
    public RandomWorld getWorld() {
        return new SpongeWorld(location.getExtent());
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
        return new SpongeBiome(chunk.getBiome(x, y, z));
    }
}
