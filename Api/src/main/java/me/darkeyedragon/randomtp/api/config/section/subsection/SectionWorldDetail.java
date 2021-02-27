package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.Offset;

public interface SectionWorldDetail {
    String getName();

    Offset getOffset();

    RandomWorld getWorld();

    boolean useWorldBorder();

    boolean needsWorldPermission();

    double getPrice();

    boolean useEco();
}
