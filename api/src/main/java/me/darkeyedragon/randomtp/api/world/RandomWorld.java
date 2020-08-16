package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RandomWorld {

    UUID getUUID();

    RandomBlock getHighestBlockAt(int x, int z);

    CompletableFuture<RandomChunk> getChunkAtAsync(int x, int z);

    RandomBlock getBlockAt(RandomLocation location);

    String getName();

    RandomBlock getBlockAt(int x, int y, int z);
}
