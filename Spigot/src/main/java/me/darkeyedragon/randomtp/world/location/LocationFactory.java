package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.config.ConfigHandler;

import java.util.Set;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public WorldConfigSection getWorldConfigSection(SectionWorldDetail sectionWorldDetail){
        Set<SectionWorldDetail> worldSet = configHandler.getSectionWorld().getWorldSet();
        WorldConfigSection worldConfigSection = worldSet.get(sectionWorldDetail);
        int offsetX;
        int offsetZ;
        int radius;
        if(worldConfigSection == null) return null;
        if (worldConfigSection.useWorldBorder()) {
            RandomWorldBorder worldBorder = sectionWorldDetail.getWorldBorder();
            offsetX = worldBorder.getCenter().getBlockX();
            offsetZ = worldBorder.getCenter().getBlockZ();
            radius = (int) Math.floor(sectionWorldDetail.getWorldBorder().getSize() / 2 - sectionWorldDetail.getWorldBorder().getWarningDistance());
        } else {
            offsetX = worldConfigSection.getX();
            offsetZ = worldConfigSection.getZ();
            radius = worldConfigSection.getRadius();
        }
        return new WorldConfigSection(offsetX, offsetZ, radius, sectionWorldDetail, worldConfigSection.useWorldBorder(), worldConfigSection.needsWorldPermission());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
