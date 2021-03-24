package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.*;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.common.config.serializer.RandomParticleSerializer;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

//TODO implement common config system
public class CommonConfigHandler implements RandomConfigHandler {

    protected final YamlConfigurationLoader loader;
    protected final RandomTeleportPluginImpl randomTeleportPlugin;
    protected ConfigurationNode root;

    public CommonConfigHandler(RandomTeleportPluginImpl randomTeleportPlugin) {
        this.randomTeleportPlugin = randomTeleportPlugin;
        loader = YamlConfigurationLoader
                .builder()
                .file(this.randomTeleportPlugin.getDataFolder())
                .defaultOptions(
                        configurationOptions -> configurationOptions.serializers(builder -> {
                                    builder.register(RandomParticle.class, RandomParticleSerializer.INSTANCE);
                                    /*builder.register(RandomBlacklist.class, RandomBlacklistSerializer.INSTANCE);
                                    builder.register(RandomBlacklist.class, RandomBlacklistSerializer.INSTANCE);
                                    builder.register(RandomBlacklist.class, RandomBlacklistSerializer.INSTANCE);*/
                                }
                        )
                )
                .build();
    }

    public void load() throws ConfigurateException {
        root = loader.load();
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
    public void saveConfig() {

    }

    //TODO implement reload
    @Override
    public boolean reload() {
        return false;
    }
}
