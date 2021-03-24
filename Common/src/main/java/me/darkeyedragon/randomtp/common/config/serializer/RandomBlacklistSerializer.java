package me.darkeyedragon.randomtp.common.config.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

//TODO make dimentions
public class RandomBlacklistSerializer implements TypeSerializer<RandomBlacklistSerializer> {

    private static final String GLOBAL = "global";
    private static final String OVERWORLD = "overworld";
    private static final String NETHER = "nether";
    private static final String END = "end";

    private RandomBlacklistSerializer() {
    }

    @Override
    public RandomBlacklistSerializer deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return null;
    }

    @Override
    public void serialize(Type type, @Nullable RandomBlacklistSerializer obj, ConfigurationNode node) throws SerializationException {

    }
}
