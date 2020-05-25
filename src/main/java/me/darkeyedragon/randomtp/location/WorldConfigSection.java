package me.darkeyedragon.randomtp.location;

import org.bukkit.World;

public class WorldConfigSection {

    private final int x;
    private final int z;
    private final int radius;
    private final World world;
    private final boolean useWorldBorder;
    private final boolean needsWorldPermission;

    public WorldConfigSection(int x, int z, int radius, World world, boolean useWorldBorder,boolean needsWorldPermission) {
        this.x = x;
        this.z = z;
        this.radius = radius;
        this.world = world;
        this.useWorldBorder = useWorldBorder;
        this.needsWorldPermission = needsWorldPermission;
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

    public boolean needsWorldPermission() {
        return needsWorldPermission;
    }
}
