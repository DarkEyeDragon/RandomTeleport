package me.darkeyedragon.randomtp.api.world.location.search;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;

public interface LocationDataProvider {

    RandomWorld getWorld();

    RandomOffset getOffset();

    int getRadius();
}
