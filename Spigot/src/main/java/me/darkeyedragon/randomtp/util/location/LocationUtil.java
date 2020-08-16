package me.darkeyedragon.randomtp.util.location;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    public static RandomLocation toRandomLocation(Location location){
        return new RandomLocation(location.getWorld().getUID(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location toLocation(RandomLocation location){
        World world = Bukkit.getWorld(location.getRandomWorld());
        return new Location(world, location.getX(), location.getY(), location.getZ());
    }
}
