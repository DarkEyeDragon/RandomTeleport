package me.darkeyedragon.randomtp.api.config;

import me.darkeyedragon.randomtp.api.config.section.*;

public interface RandomConfigHandler {
    SectionDebug getSectionDebug();
    SectionEconomy getSectionEconomy();
    SectionMessage getSectionMessage();
    SectionQueue getSectionQueue();
    SectionTeleport getSectionTeleport();

    SectionWorld getSectionWorld();

    SectionBlacklist getSectionBlacklist();

    void populateWorldConfigSection();

    void saveConfig();

    boolean reload();
}
