package me.darkeyedragon.randomtp.common.util;

import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChunkIterator implements Iterator<CompletableFuture<RandomChunkSnapshot>> {

    private static final Map<Integer, Direction> directionMap;

    static {
        directionMap = new HashMap<>();
        for (int i = 0; i < Direction.values().length; i++) {
            directionMap.put(i, Direction.getDirection(i));
        }
    }

    private final RandomChunkSnapshot startChunk;
    private int counter;

    public ChunkIterator(RandomChunkSnapshot startChunk) {
        this.startChunk = startChunk;
        counter = 0;
    }

    public boolean hasNext() {
        return counter < 7;
    }

    public CompletableFuture<RandomChunkSnapshot> next() {
        Direction direction = directionMap.get(counter++);
        int finalX = startChunk.getX() + direction.getX();
        int finalZ = startChunk.getZ() + direction.getZ();
        return startChunk.getWorld().getChunkAtAsync(startChunk.getWorld(), finalX, finalZ);
    }
}
