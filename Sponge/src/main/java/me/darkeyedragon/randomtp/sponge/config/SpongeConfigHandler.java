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

    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode root;
    @Override
    public void init() {
        loader = HoconConfigurationLoader.builder().setPath(new File("randomtp.conf").toPath()).build();
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public SectionPlugin getSectionPlugin() {
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
    
}
