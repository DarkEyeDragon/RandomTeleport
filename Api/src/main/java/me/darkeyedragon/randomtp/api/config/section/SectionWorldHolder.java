package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

import java.util.Set;

public interface SectionWorldHolder {
    Set<SectionWorldDetail> getSectionWorldDetailSet();

    boolean add(SectionWorldDetail sectionWorldDetail);

    SectionWorldHolder setSectionDetail(SectionWorldDetail sectionWorld);

    Set<RandomWorld> getWorlds();

    boolean contains(RandomWorld world);

    boolean remove(RandomWorld world);

    SectionWorldDetail getSectionWorldDetail(RandomWorld randomWorld);
}
