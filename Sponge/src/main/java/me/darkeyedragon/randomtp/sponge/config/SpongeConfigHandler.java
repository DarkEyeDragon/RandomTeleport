package me.darkeyedragon.randomtp.sponge.config;

import me.darkeyedragon.randomtp.api.config.Blacklist;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.*;
import me.darkeyedragon.randomtp.sponge.config.section.*;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SpongeConfigHandler implements RandomConfigHandler {

    private ConfigDebug configDebug;
    private ConfigBlacklist configBlacklist;
    private ConfigEconomy configEconomy;
    private ConfigMessage configMessage;
    private ConfigPlugin configPlugin;
    private ConfigQueue configQueue;
    private ConfigTeleport configTeleport;
    private ConfigWorld configWorld;

    private HoconConfigurationLoader loader;

    @Override
    public void init() {
        loader = HoconConfigurationLoader.builder().setPath(new File("randomtp.conf").toPath()).build();
        try {
            CommentedConfigurationNode root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SectionDebug getSectionDebug() {
        return configDebug;
    }

    @Override
    public SectionEconomy getSectionEconomy() {
        return configEconomy;
    }

    @Override
    public SectionMessage getSectionMessage() {
        return configMessage;
    }

    @Override
    public SectionPlugin getSectionPlugin() {
        return configPlugin;
    }

    @Override
    public SectionQueue getSectionQueue() {
        return configQueue;
    }

    @Override
    public SectionTeleport getSectionTeleport() {
        return configTeleport;
    }

    @Override
    public SectionWorld getSectionWorld() {
        return configWorld;
    }

}
