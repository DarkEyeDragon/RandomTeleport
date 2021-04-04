package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionWorldHolder;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.config.datatype.World;

import java.util.Set;

public class CommonSectionWorldHolder implements SectionWorldHolder {

    private final Set<World> worlds;

    public CommonSectionWorldHolder(Set<World> worlds) {
        this.worlds = worlds;
    }

    @Override
    public Set<SectionWorldDetail> getSectionWorldDetailSet() {
        return null;
        //return worlds;
    }

    @Override
    public boolean add(SectionWorldDetail sectionWorldDetail) {
        //return sectionWorldDetailSet.add(sectionWorldDetail);
        return false;

    }

    @Override
    public SectionWorldHolder setSectionDetail(SectionWorldDetail sectionWorld) {
        return null;
    }

    @Override
    public Set<RandomWorld> getWorlds() {
        //return sectionWorldDetailSet.stream().map(SectionWorldDetail::getWorld).collect(Collectors.toSet());
        return null;

    }

    @Override
    public boolean contains(RandomWorld world) {
        /*for (SectionWorldDetail sectionWorldDetail : sectionWorldDetailSet) {
            if (sectionWorldDetail.getWorld().equals(world)) return true;
        }*/
        return false;
    }

    @Override
    public boolean remove(RandomWorld world) {
        return false;
    }

    @Override
    public SectionWorldDetail getSectionWorldDetail(RandomWorld randomWorld) {
        /*for (SectionWorldDetail sectionWorldDetail : sectionWorldDetailSet) {
            if (sectionWorldDetail.getWorld().equals(randomWorld)) {
                return sectionWorldDetail;
            }
        }*/
        return null;
    }
}
