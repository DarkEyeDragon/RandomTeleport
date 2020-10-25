package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.common.config.Blacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import ninja.leaping.configurate.objectmapping.Setting;

public class ConfigBlacklist implements SectionBlacklist {

    @Setting
    Blacklist blacklist;

    @Override
    public Blacklist getBlacklist() {
        return null;
    }
}
