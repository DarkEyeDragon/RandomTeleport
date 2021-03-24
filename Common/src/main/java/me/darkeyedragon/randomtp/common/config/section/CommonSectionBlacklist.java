package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;

public class CommonSectionBlacklist implements SectionBlacklist {

    private RandomBlacklist blacklist;

    @Override
    public RandomBlacklist getBlacklist() {
        return blacklist;
    }
}
