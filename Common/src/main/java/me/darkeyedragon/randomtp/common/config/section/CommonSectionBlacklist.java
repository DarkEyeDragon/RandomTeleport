package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionBlacklist implements SectionBlacklist {

    private final RandomBlacklist blacklist;

    public CommonSectionBlacklist(RandomBlacklist blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public RandomBlacklist getBlacklist() {
        return blacklist;
    }
}
