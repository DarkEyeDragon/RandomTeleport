package me.darkeyedragon.randomtp.world.location;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.location.Offset;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.config.ConfigHandler;

public class LocationFactory {

    private final ConfigHandler configHandler;

    public LocationFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public WorldConfigSection getWorldConfigSection(RandomWorld randomWorld){
        SectionWorldDetail sectionWorldDetail = configHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
        Offset offset = new Offset();
        if(sectionWorldDetail == null) return null;
        if (sectionWorldDetail.useWorldBorder()) {
            RandomWorldBorder worldBorder = sectionWorldDetail.getWorld().getWorldBorder();
            offset.setX(worldBorder.getCenter().getX());
            offset.setZ(worldBorder.getCenter().getZ());
            offset.setRadius((int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance()));
        } else {
            offset = sectionWorldDetail.getOffset();
        }
        return new WorldConfigSection(offset, sectionWorldDetail.getWorld(), sectionWorldDetail.useWorldBorder(), sectionWorldDetail.needsWorldPermission());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
