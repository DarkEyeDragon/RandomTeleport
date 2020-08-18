package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;

import java.util.Map;
import java.util.Set;

public interface SectionWorld {
    Map<RandomWorld, SectionWorldDetail> getWorldSet();

}
