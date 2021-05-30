package me.darkeyedragon.randomtp.common.world.location.search;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;
import me.darkeyedragon.randomtp.api.world.location.search.LocationDataProvider;

public class CommonLocationDataProvider implements LocationDataProvider {

    protected RandomWorld world;
    protected RandomOffset offset;
    protected int radius;

    public CommonLocationDataProvider(RandomWorld world, RandomOffset offset, int radius) {
        this.world = world;
        this.offset = offset;
        this.radius = radius;
    }

    @Override
    public RandomWorld getWorld() {
        return world;
    }

    @Override
    public RandomOffset getOffset() {
        return offset;
    }

    public int getRadius() {
        return radius;
    }
}
