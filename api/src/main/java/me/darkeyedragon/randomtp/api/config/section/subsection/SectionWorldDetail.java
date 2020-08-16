package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;

import java.util.UUID;

public interface SectionWorldDetail {
    String getName();

    int getRadius();

    int getX();

    int getZ();

    RandomWorld getWorld();

    RandomWorldBorder getWorldBorder();
}
