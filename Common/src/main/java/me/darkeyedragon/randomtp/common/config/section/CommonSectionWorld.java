package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

import java.util.Set;

public class CommonSectionWorld implements SectionWorld {

    Set<SectionWorldDetail> sectionWorldDetailSet;


    @Override
    public Set<SectionWorldDetail> getSectionWorldDetailSet() {
        return sectionWorldDetailSet;
    }

    @Override
    public boolean add(SectionWorldDetail sectionWorldDetail) {
        return false;
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
