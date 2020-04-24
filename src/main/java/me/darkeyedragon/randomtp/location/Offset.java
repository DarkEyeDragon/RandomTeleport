package me.darkeyedragon.randomtp.location;

import org.bukkit.World;

public class Offset {

    private final int x;
    private final int z;
    private final int radius;
    private final World world;
    private final boolean useWorldBorder;

    public Offset(int x, int z, int radius, World world, boolean useWorldBorder) {
        this.x = x;
        this.z = z;
        this.radius = radius;
        this.world = world;
        this.useWorldBorder = useWorldBorder;
    }

    public int getX() {
        return x;
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

    public boolean useWorldBorder() {
        return useWorldBorder;
    }
}
