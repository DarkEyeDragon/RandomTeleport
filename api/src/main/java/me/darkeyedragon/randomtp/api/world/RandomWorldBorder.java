package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface RandomWorldBorder {
    RandomLocation getCenter();
    double getSize();

    int getWarningDistance();
}
