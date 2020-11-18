package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class SpigotWorldBorder implements RandomWorldBorder {

    private final WorldBorder worldBorder;

    public SpigotWorldBorder(WorldBorder worldBorder) {
        this.worldBorder = worldBorder;
    }

    @Override
    public RandomLocation getCenter() {
        SpigotWorld spigotWorld = new SpigotWorld(worldBorder.getCenter().getWorld());
        Location location = worldBorder.getCenter();
        return new RandomLocation(spigotWorld, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public double getSize() {
        return worldBorder.getSize();
    }

    @Override
    public int getWarningDistance() {
        return worldBorder.getWarningDistance();
    }
}
