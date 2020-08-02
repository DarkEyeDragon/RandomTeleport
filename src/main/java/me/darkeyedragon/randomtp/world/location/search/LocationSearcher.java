package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.Location;

import java.util.concurrent.CompletableFuture;

public interface LocationSearcher {

    CompletableFuture<Location> getRandom(WorldConfigSection worldConfigSection);

    boolean isSafe(Location location);
}
