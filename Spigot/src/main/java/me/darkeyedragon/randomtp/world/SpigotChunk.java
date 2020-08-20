package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import org.bukkit.Chunk;

public class SpigotChunk implements RandomChunk {

    private final Chunk chunk;

    public SpigotChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public RandomWorld getWorld() {
        return new SpigotWorld(chunk.getWorld());
    }

    @Override
    public int getX() {
        return chunk.getX();
    }

    @Override
    public int getZ() {
        return chunk.getZ();
    }
}
