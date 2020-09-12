package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

public class WorldConfigSection implements SectionWorldDetail {

    private final int x;
    private final int z;
    private final int radius;
    private final RandomWorld world;
    private final boolean useWorldBorder;
    private final boolean needsWorldPermission;

    public WorldConfigSection(int x, int z, int radius, RandomWorld world, boolean useWorldBorder, boolean needsWorldPermission) {
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

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public RandomWorld getWorld() {
        return world;
    }

    @Override
    public boolean useWorldBorder() {
        return useWorldBorder;
    }

    @Override
    public boolean needsWorldPermission() {
        return needsWorldPermission;
    }
}
