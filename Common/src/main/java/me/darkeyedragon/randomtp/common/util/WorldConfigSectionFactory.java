package me.darkeyedragon.randomtp.common.util;

import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.common.config.ConfigHandler;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.api.world.location.Offset;

public class WorldConfigSectionFactory {

    private final ConfigHandler configHandler;

    public WorldConfigSectionFactory(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    public SectionWorld getWorldConfigSection(RandomWorld randomWorld) {
        SectionWorldDetail sectionWorldDetail = configHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
        Offset offset = new Offset();
        if (sectionWorldDetail == null) return null;
        if (sectionWorldDetail.useWorldBorder()) {
            RandomWorldBorder worldBorder = sectionWorldDetail.getWorld().getWorldBorder();
            offset.setX(worldBorder.getCenter().getX());
            offset.setZ(worldBorder.getCenter().getZ());
            offset.setRadius((int) Math.floor(worldBorder.getSize() / 2 - worldBorder.getWarningDistance()));
        } else {
            offset = sectionWorldDetail.getOffset());
        }
        return new WorldConfigSection(offset, sectionWorldDetail.getWorld(), sectionWorldDetail.useWorldBorder(), sectionWorldDetail.needsWorldPermission());
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
