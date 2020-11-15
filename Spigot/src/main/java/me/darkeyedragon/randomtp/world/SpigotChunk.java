package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;

public class SpigotChunk implements RandomChunk {

    private final ChunkSnapshot chunk;

    public SpigotChunk(ChunkSnapshot chunk) {
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
}
