package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.config.ConfigHandler;

import java.util.Map;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public WorldConfigSection getWorldConfigSection(RandomWorld randomWorld){
        Map<RandomWorld, SectionWorldDetail> worldMap = configHandler.getSectionWorld().getWorldSet();
        SectionWorldDetail sectionWorldDetail = worldMap.get(randomWorld);
        int offsetX = 0;
        int offsetZ = 0;
        int radius = 0;
        if(sectionWorldDetail == null) return null;
        if (sectionWorldDetail.useWorldBorder()) {
            RandomWorldBorder worldBorder = sectionWorldDetail.getWorld().getWorldBorder();
            offsetX = worldBorder.getCenter().getX();
            offsetZ = worldBorder.getCenter().getZ();
            radius = (int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance());
        } else {
            offsetX = sectionWorldDetail.getX();
            offsetZ = sectionWorldDetail.getZ();
            radius = sectionWorldDetail.getRadius();
        }
        return new WorldConfigSection(offsetX, offsetZ, radius, sectionWorldDetail.getWorld(), sectionWorldDetail.useWorldBorder(), sectionWorldDetail.needsWorldPermission());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
