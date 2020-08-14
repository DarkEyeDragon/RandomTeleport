package me.darkeyedragon.randomtp.api.world.location;

import me.darkeyedragon.randomtp.api.world.RandomWorld;

public class RandomLocation {

    private final RandomWorld world;
    private final int x;
    private final int y;
    private final int z;

    public RandomLocation(RandomWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RandomWorld getWorld() {
        return world;
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
}
