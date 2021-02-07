package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.teleport.RandomParticle;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.Offset;
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

    /**
     * @param world the {@link RandomWorld} you want to get the {@link RandomWorldBorder} offset of
     * @return the {@link Offset} of the {@link RandomWorldBorder}
     */
    static Offset getOffset(RandomWorld world) {
        RandomWorldBorder worldBorder = world.getWorldBorder();
        RandomLocation center = worldBorder.getCenter();
        Offset offset = new Offset();
        offset.setX(center.getBlockX());
        offset.setZ(center.getBlockZ());
        offset.setRadius((int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance()));
        return offset;
    }

    void spawnParticle(RandomParticle<?> particle, RandomLocation spawnLoc, int amount);
}
