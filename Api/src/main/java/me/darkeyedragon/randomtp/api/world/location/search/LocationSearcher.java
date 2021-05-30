package me.darkeyedragon.randomtp.api.world.location.search;


import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.concurrent.CompletableFuture;

public interface LocationSearcher {

    CompletableFuture<RandomLocation> getRandom(LocationDataProvider dataProvider);

    boolean isSafe(RandomLocation location);

    boolean isSafeForPlugins(RandomLocation location);
}
