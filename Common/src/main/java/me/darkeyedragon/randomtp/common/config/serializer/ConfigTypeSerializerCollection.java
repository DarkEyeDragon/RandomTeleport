package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.util.Set;

public class ConfigTypeSerializerCollection {

    private final RandomTeleportPlugin<?> plugin;

    public ConfigTypeSerializerCollection(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
    }

    public TypeSerializerCollection build() {
        TypeSerializerCollection.Builder builder = TypeSerializerCollection.builder();
        builder.registerExact(RandomDimensionData.class, RandomDimensionDataSerializer.INSTANCE);
        builder.register(RandomParticle.class, RandomParticleSerializer.INSTANCE);
        builder.register(SectionWorld.class, SectionWorldSerializer.INSTANCE);
        builder.register(SectionBlacklist.class, new SectionBlacklistSerializer(plugin));
        builder.register(new TypeToken<Set<RandomMaterial>>() {
        }, new RandomMaterialSerializer(plugin));
        builder.registerAll(ConfigurateComponentSerializer.configurate().serializers());
        return builder.build();
    }

}
