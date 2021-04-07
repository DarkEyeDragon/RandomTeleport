package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.*;

//TODO implement common config system
public class ConfigHandler implements RandomConfigHandler {

    @Override
    public SectionDebug getSectionDebug() {
        return null;
    }

    @Override
    public SectionEconomy getSectionEconomy() {
        return null;
    }

    @Override
    public SectionMessage getSectionMessage() {
        return null;
    }

    @Override
    public SectionQueue getSectionQueue() {
        return null;
    }

    @Override
    public SectionTeleport getSectionTeleport() {
        return null;
    }

    @Override
    public SectionWorld getSectionWorld() {
        return null;
    }

    @Override
    public SectionBlacklist getSectionBlacklist() {
        return null;
    }

    @Override
    public void populateWorldConfigSection() {

    }

    @Override
    public void saveConfig() {

    }

    @Override
    public void reload() {

    }
}
