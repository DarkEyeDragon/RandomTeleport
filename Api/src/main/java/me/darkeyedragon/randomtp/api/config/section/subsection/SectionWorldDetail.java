package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;

public interface SectionWorldDetail extends SubSection {
    String getName();

    RandomOffset getOffset();

    RandomWorld getWorld();

    boolean useWorldBorder();

    boolean needsWorldPermission();

    double getPrice();

    boolean useEco();
}
