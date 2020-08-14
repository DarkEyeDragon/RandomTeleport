package me.darkeyedragon.randomtp.api.world.location.search;


import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.concurrent.CompletableFuture;

public interface LocationSearcher {

    CompletableFuture<RandomLocation> getRandom(SectionWorldDetail sectionWorldDetail);

}
