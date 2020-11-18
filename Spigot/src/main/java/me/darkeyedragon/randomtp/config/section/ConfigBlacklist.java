package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.common.config.Blacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;

public class ConfigBlacklist implements SectionBlacklist {

    final Blacklist blacklist;

    public ConfigBlacklist(Blacklist blacklist) {
        this.blacklist = blacklist;
    }


    @Override
    public Blacklist getBlacklist() {
        return blacklist;
    }
}
