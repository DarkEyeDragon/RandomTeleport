package me.darkeyedragon.randomtp.api.world.location;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;

public class RandomLocation {

    private final RandomWorld randomWorld;
    private final int x;
    private final int y;
    private final int z;
    private int tries;


    public RandomLocation(RandomWorld world, int x, int y, int z) {
        this(world, x, y, z, -1);
    }

    public RandomLocation(RandomWorld randomWorld, int x, int y, int z, int tries) {
        this.randomWorld = randomWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tries = tries;
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

    public RandomWorld getRandomWorld() {
        return randomWorld;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public RandomWorld getWorld() {
        return randomWorld;
    }

    public RandomBlock getBlock() {
        return randomWorld.getBlockAt(this);
    }
}
