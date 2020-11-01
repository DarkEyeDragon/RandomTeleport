package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

import java.util.List;
import java.util.Set;

public class ConfigWorld implements SectionWorld {


    @Override
    public Set<SectionWorldDetail> getSectionWorldDetailSet() {
        return null;
    }

    @Override
    public LocationQueue add(SectionWorldDetail sectionWorldDetail) {
        return null;
    }

    @Override
    public SectionWorld setSectionDetail(SectionWorldDetail sectionWorld) {
        return null;
    }

    @Override
    public Set<RandomWorld> getWorlds() {
        return null;
    }

    @Override
    public boolean contains(RandomWorld world) {
        return false;
    }

    @Override
    public boolean remove(RandomWorld world) {
        return false;
    }

    @Override
    public SectionWorldDetail getSectionWorldDetail(RandomWorld randomWorld) {
        return null;
    }
}
