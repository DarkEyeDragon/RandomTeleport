package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionDebug;
import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

public class CommonConfigHandler implements RandomConfigHandler {

    protected final AbstractConfigurationLoader<CommentedConfigurationNode> loader;
    protected final RandomTeleportPluginImpl randomTeleportPlugin;
    protected ConfigurationNode root;
    protected Configuration configuration;

    public CommonConfigHandler(RandomTeleportPluginImpl randomTeleportPlugin, AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader) {
        this.randomTeleportPlugin = randomTeleportPlugin;
        loader = configurationLoader;
    }

    public void load() throws ConfigurateException {
        root = loader.load();
        configuration = root.get(Configuration.class);
    }

    @Override
    public SectionDebug getSectionDebug() {
        return configuration.getDebug();
    }

    @Override
    public SectionEconomy getSectionEconomy() {
        return configuration.getEconomy();
    }

    @Override
    public SectionMessage getSectionMessage() {
        Thread.dumpStack();
        return configuration.getMessages();
    }

    @Override
    public SectionQueue getSectionQueue() {
        return configuration.getQueue();
    }

    @Override
    public SectionTeleport getSectionTeleport() {
        return configuration.getTeleport();
    }

    @Override
    public SectionWorld getSectionWorld() {
        return configuration.getWorlds();
    }

    @Override
    public SectionBlacklist getSectionBlacklist() {
        return configuration.getBlacklist();
    }

    @Override
    public void populateWorldConfigSection() {

    }

    @Override
    public void saveConfig() {
        //TODO implement saveconfig
        //configuration.save();
    }

    //TODO implement reload
    @Override
    public boolean reload() {
        try {
            load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        return true;
    }
}
