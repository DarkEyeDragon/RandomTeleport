package me.darkeyedragon.randomtp.api.world.location;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;

public class RandomLocation {

    private final RandomWorld randomWorld;
    private final int x;
    private final int y;
    private final int z;

    public RandomLocation(RandomWorld world, int x, int y, int z) {
        this.randomWorld = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public RandomWorld getWorld() {
        return randomWorld;
    }

    public RandomBlock getBlock() {
        return randomWorld.getBlockAt(this);
    }
}
