package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.common.config.datatype.Blacklist;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class RandomBlacklistSerializer implements TypeSerializer<RandomBlacklist> {
    public static final RandomBlacklistSerializer INSTANCE = new RandomBlacklistSerializer();

    @Override
    public RandomBlacklist deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Map<Dimension, RandomDimensionData> dimensionData = node.get(new TypeToken<Map<Dimension, RandomDimensionData>>() {
        });
        return new Blacklist(dimensionData);
    }

    @Override
    public void serialize(Type type, @Nullable RandomBlacklist obj, ConfigurationNode node) throws SerializationException {

    }
}
