package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.Blacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import org.bukkit.Material;

public class ConfigBlacklist implements SectionBlacklist<Material> {

    final Blacklist<Material> blacklist;

    public ConfigBlacklist(Blacklist<Material> blacklist) {
        this.blacklist = blacklist;
    }


    @Override
    public Blacklist<Material> getBlacklist() {
        return blacklist;
    }
}
