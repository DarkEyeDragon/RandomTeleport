package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;

public interface SectionWorldDetail {
    String getName();

    int getRadius();

    int getX();

    int getZ();

    RandomWorld getWorld();

    boolean useWorldBorder();

    boolean needsWorldPermission();
}
