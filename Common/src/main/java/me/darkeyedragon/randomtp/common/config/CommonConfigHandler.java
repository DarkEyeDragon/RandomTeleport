package me.darkeyedragon.randomtp.common.config;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionDebug;
import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.common.config.serializer.RandomDimensionDataSerializer;
import me.darkeyedragon.randomtp.common.config.serializer.RandomMaterialSerializer;
import me.darkeyedragon.randomtp.common.config.serializer.RandomParticleSerializer;
import me.darkeyedragon.randomtp.common.config.serializer.SectionBlacklistSerializer;
import me.darkeyedragon.randomtp.common.config.serializer.SectionWorldSerializer;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Paths;
import java.util.Set;

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
                                    //builder.register(RandomBlacklist.class, RandomBlacklistSerializer.INSTANCE);
                                    builder.registerExact(RandomDimensionData.class, RandomDimensionDataSerializer.INSTANCE);
                                    builder.register(RandomParticle.class, RandomParticleSerializer.INSTANCE);
                                    builder.register(SectionWorld.class, SectionWorldSerializer.INSTANCE);
                                    builder.register(SectionBlacklist.class, new SectionBlacklistSerializer(randomTeleportPlugin));
                                    builder.register(new TypeToken<Set<RandomMaterial>>() {
                                    }, new RandomMaterialSerializer(randomTeleportPlugin));
                                    builder.registerAll(ConfigurateComponentSerializer.configurate().serializers());
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
