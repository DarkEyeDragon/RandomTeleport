package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.Offset;

public class WorldConfigSection implements SectionWorldDetail {

    private final Offset offset;
    private final RandomWorld world;
    private final boolean useWorldBorder;
    private final boolean needsWorldPermission;
    private final double price;

    public WorldConfigSection(Offset offset, RandomWorld world, double price, boolean useWorldBorder, boolean needsWorldPermission) {
        this.offset = offset;
        this.world = world;
        this.useWorldBorder = useWorldBorder;
        this.needsWorldPermission = needsWorldPermission;
        this.price = price;
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public Offset getOffset() {
        return offset;
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

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean useEco() {
        return price > 0;
    }
}
