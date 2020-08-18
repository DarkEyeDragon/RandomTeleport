package me.darkeyedragon.randomtp.util.location;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    public static RandomLocation toRandomLocation(Location location){
        return new RandomLocation(WorldUtil.toRandomWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location toLocation(RandomLocation location){
        return new Location(WorldUtil.toWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }
}
