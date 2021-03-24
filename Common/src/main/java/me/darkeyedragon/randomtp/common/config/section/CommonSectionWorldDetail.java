package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.location.Offset;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionWorldDetail implements SectionWorldDetail {

    private String name;
    private Offset offset;
    private RandomWorld world;
    private boolean worldBorder;
    private boolean worldPermission;
    private double price;
    private boolean eco;

    @Override
    public String getName() {
        return name;
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
        return worldBorder;
    }

    @Override
    public boolean needsWorldPermission() {
        return worldPermission;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean useEco() {
        return eco;
    }
}
