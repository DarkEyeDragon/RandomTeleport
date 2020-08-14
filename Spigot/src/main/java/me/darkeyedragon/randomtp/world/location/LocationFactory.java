package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.config.ConfigHandler;
import org.bukkit.World;

import java.util.Map;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public WorldConfigSection getWorldConfigSection(World world){
        Map<World, WorldConfigSection> worldOffsetMap = configHandler.getSectionWorld().getWorldConfigSectionMap();
        WorldConfigSection worldConfigSection = worldOffsetMap.get(world);
        int offsetX;
        int offsetZ;
        int radius;
        if(worldConfigSection == null) return null;
        if (worldConfigSection.useWorldBorder()) {
            offsetX = world.getWorldBorder().getCenter().getBlockX();
            offsetZ = world.getWorldBorder().getCenter().getBlockZ();
            radius = (int) Math.floor(world.getWorldBorder().getSize() / 2 - world.getWorldBorder().getWarningDistance());
        } else {
            offsetX = worldConfigSection.getX();
            offsetZ = worldConfigSection.getZ();
            radius = worldConfigSection.getRadius();
        }
        return new WorldConfigSection(offsetX, offsetZ, radius, world, worldConfigSection.useWorldBorder(), worldConfigSection.needsWorldPermission());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
