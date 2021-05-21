package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.common.world.CommonBlockType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomBlockTypeSerializer implements TypeSerializer<Set<RandomBlockType>> {

    @Override
    public Set<RandomBlockType> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Set<RandomMaterial> randomMaterial = node.get(new TypeToken<Set<RandomMaterial>>() {
        });
        return randomMaterial.stream().map(CommonBlockType::new).collect(Collectors.toSet());
    }

    @Override
    public void serialize(Type type, @Nullable Set<RandomBlockType> obj, ConfigurationNode node) throws SerializationException {

    }
}
