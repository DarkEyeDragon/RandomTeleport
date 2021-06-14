package me.darkeyedragon.randomtp.common.world.location;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;

public class LocationFactory {

    private final RandomConfigHandler configHandler;

    public LocationFactory(RandomConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    /*public CommonSectionWorldDetail getWorldConfigSection(RandomWorld randomWorld){
        SectionWorldDetail sectionWorldDetail = configHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
        RandomOffset offset;
        if(sectionWorldDetail == null) return null;
        if (sectionWorldDetail.useWorldBorder()) {
            RandomWorldBorder worldBorder = sectionWorldDetail.getWorld().getWorldBorder();
            RandomLocation center = worldBorder.getCenter();
            offset = new Offset(center.getBlockX(), center.getBlockY(), (int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance()));
        } else {
            offset = sectionWorldDetail.getOffset();
        }
        return new CommonSectionWorldDetail(offset, sectionWorldDetail.getWorld(), sectionWorldDetail.getPrice(), sectionWorldDetail.useWorldBorder(), sectionWorldDetail.needsWorldPermission());
    }*/

    public RandomConfigHandler getConfigHandler() {
        return configHandler;
    }
}
