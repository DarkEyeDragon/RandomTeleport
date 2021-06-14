package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface RandomWorldBorder {
    RandomLocation getCenter();

    /**
     * @return the total width of the {@link RandomWorldBorder}
     */
    double getSize();

    int getWarningDistance();
}
