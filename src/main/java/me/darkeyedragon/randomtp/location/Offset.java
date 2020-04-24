package me.darkeyedragon.randomtp.location;

import org.bukkit.World;

public class Offset {

    private final int X;
    private final int z;
    private final int radius;
    private final World world;

    public Offset(int x, int z, int radius, World world) {
        X = x;
        this.z = z;
        this.radius = radius;
        this.world = world;
    }

    public int getX() {
        return X;
    }

    public int getZ() {
        return z;
    }

    public int getRadius() {
        return radius;
    }

    public World getWorld() {
        return world;
    }
}
