package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Set;

public class RandomMaterialSerializer implements TypeSerializer<Set<RandomMaterial>> {

    private final RandomTeleportPluginImpl impl;

    public RandomMaterialSerializer(RandomTeleportPluginImpl impl) {
        this.impl = impl;
    }

    @Override
    public Set<RandomMaterial> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String material = node.get(new TypeToken<String>() {
        });
        return impl.getMaterialHandler().getFromTag(material);
    }

    @Override
    public void serialize(Type type, @Nullable Set<RandomMaterial> obj, ConfigurationNode node) throws SerializationException {

    }
}
