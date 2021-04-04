package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.api.config.section.*;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.common.config.serializer.*;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Paths;

//TODO implement common config system
public class CommonConfigHandler implements RandomConfigHandler {

    protected final YamlConfigurationLoader loader;
    protected final RandomTeleportPluginImpl randomTeleportPlugin;
    protected ConfigurationNode root;
    protected Configuration configuration;

    public CommonConfigHandler(RandomTeleportPluginImpl randomTeleportPlugin) {
        this.randomTeleportPlugin = randomTeleportPlugin;
        loader = YamlConfigurationLoader
                .builder()
                .path(Paths.get(this.randomTeleportPlugin.getDataFolder().getPath(), "config.yml"))
                .defaultOptions(
                        configurationOptions -> configurationOptions.serializers(builder -> {
                                    builder.register(RandomBlacklist.class, RandomBlacklistSerializer.INSTANCE);
                                    builder.registerExact(RandomDimensionData.class, RandomDimensionDataSerializer.INSTANCE);
                                    builder.register(RandomParticle.class, RandomParticleSerializer.INSTANCE);
                                    builder.register(SectionWorldHolder.class, SectionWorldSerializer.INSTANCE);
                                    builder.register(SectionBlacklist.class, SectionBlacklistSerializer.INSTANCE);
                                }
                        )
                )
                .build();
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
    public SectionWorldHolder getSectionWorld() {
        return configuration.getWorlds();
    }

    @Override
    public SectionBlacklist getSectionBlacklist() {
        return configuration.getBlacklist();
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
