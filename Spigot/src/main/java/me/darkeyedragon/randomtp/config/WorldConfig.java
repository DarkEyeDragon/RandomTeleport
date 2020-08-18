package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;

import java.util.Map;

public class WorldConfig implements SectionWorld {

    private final Map<RandomWorld, SectionWorldDetail> worldSet;

    public WorldConfig(Map<RandomWorld, SectionWorldDetail> worldSet) {
        this.worldSet = worldSet;
    }

    @Override
    public Map<RandomWorld, SectionWorldDetail> getWorldSet() {
        return worldSet;
    }
}
