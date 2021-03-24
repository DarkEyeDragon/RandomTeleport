package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;

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
     * @return the {@link RandomOffset} of the {@link RandomWorldBorder}
     */
    static RandomOffset getOffset(RandomWorld world) {
        RandomWorldBorder worldBorder = world.getWorldBorder();
        RandomLocation center = worldBorder.getCenter();
        RandomOffset offset = new RandomOffset() {
            private int x;
            private int z;
            private int radius;

            @Override
            public int getX() {
                return x;
            }

            @Override
            public void setX(int x) {
                this.x = x;
            }

            @Override
            public int getZ() {
                return z;
            }

            @Override
            public void setZ(int z) {
                this.z = z;
            }

            @Override
            public int getRadius() {
                return radius;
            }

            @Override
            public void setRadius(int radius) {
                this.radius = radius;
            }
        };
        offset.setX(center.getBlockX());
        offset.setZ(center.getBlockZ());
        offset.setRadius((int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance()));
        return offset;
    }

    boolean isChunkLoaded(int x, int z);

    void spawnParticle(String particleId, RandomLocation spawnLoc, int amount);
}
