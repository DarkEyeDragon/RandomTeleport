package me.darkeyedragon.randomtp.common.config.serializer;

import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class RandomBlockTypeSerializer implements TypeSerializer<RandomBlockType> {
    @Override
    public RandomBlockType deserialize(Type type, ConfigurationNode node) throws SerializationException {

        return null;
    }

    @Override
    public void serialize(Type type, @Nullable RandomBlockType obj, ConfigurationNode node) throws SerializationException {

    }
}
