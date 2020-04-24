package me.darkeyedragon.randomtp.location;

import me.darkeyedragon.randomtp.config.ConfigHandler;
import org.bukkit.World;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public Offset getOffset(World world){
        int offsetX;
        int offsetZ;
        int radius;
        if (configHandler.useWorldBorder()) {
            offsetX = world.getWorldBorder().getCenter().getBlockX();
            offsetZ = world.getWorldBorder().getCenter().getBlockZ();
            radius = (int) Math.floor(world.getWorldBorder().getSize() / 2 - world.getWorldBorder().getWarningDistance());
        } else {
            offsetX = configHandler.getOffsetX();
            offsetZ = configHandler.getOffsetZ();
            radius = configHandler.getRadius();
        }
        return new Offset(offsetX, offsetZ, radius, world);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
