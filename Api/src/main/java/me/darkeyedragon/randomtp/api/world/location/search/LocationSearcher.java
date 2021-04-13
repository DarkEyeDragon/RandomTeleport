package me.darkeyedragon.randomtp.api.world.location.search;


import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.concurrent.CompletableFuture;

public interface LocationSearcher {

    CompletableFuture<RandomLocation> getRandom(ConfigWorld configWorld);

    boolean isSafe(RandomLocation location);

    boolean isSafeForPlugins(RandomLocation location);
}
