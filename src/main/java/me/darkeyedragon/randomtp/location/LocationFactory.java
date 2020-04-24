package me.darkeyedragon.randomtp.location;

import me.darkeyedragon.randomtp.config.ConfigHandler;
import org.bukkit.World;

import java.util.Map;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public Offset getOffset(World world){
        Map<World, Offset> worldOffsetMap = configHandler.getOffsets();
        Offset offset = worldOffsetMap.get(world);
        int offsetX;
        int offsetZ;
        int radius;
        if (offset.useWorldBorder()) {
            offsetX = world.getWorldBorder().getCenter().getBlockX();
            offsetZ = world.getWorldBorder().getCenter().getBlockZ();
            radius = (int) Math.floor(world.getWorldBorder().getSize() / 2 - world.getWorldBorder().getWarningDistance());
        } else {
            offsetX = offset.getX();
            offsetZ = offset.getZ();
            radius = offset.getRadius();
        }
        return new Offset(offsetX, offsetZ, radius, world, offset.useWorldBorder());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
