package me.darkeyedragon.randomtp.api.world;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RandomWorld {

    UUID getUUID();

    RandomBlock getHighestBlockAt(int x, int z);

    CompletableFuture<RandomChunkSnapshot> getChunkAtAsync(RandomWorld world, int x, int z);

    RandomBlock getBlockAt(RandomLocation location);

    String getName();

    RandomBlock getBlockAt(int x, int y, int z);

    RandomWorldBorder getWorldBorder();

    RandomEnvironment getEnvironment();

    /**
     * This method is required to be overridden to prevent duplicate worlds
     *
     * @param object the object you want to compare to
     * @return true if the world is the same
     */
    boolean equals(Object object);

    void spawnParticle(Particle particle, RandomLocation spawnLoc, int amount);
}
