package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public abstract class CommonChunkSnapshot implements RandomChunkSnapshot {

    @NotNull
    @Override
    public Iterator<CompletableFuture<RandomChunkSnapshot>> iterator() {
        return new ChunkIterator(this);
    }

}
