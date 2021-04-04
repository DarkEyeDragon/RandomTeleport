package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.common.config.datatype.DimensionData;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class RandomDimensionDataSerializer implements TypeSerializer<RandomDimensionData> {

    public static final RandomDimensionDataSerializer INSTANCE = new RandomDimensionDataSerializer();

    @Override
    public RandomDimensionData deserialize(Type type, ConfigurationNode node) throws SerializationException {

        return node.get(new TypeToken<DimensionData>() {
        });
    }

    @Override
    public void serialize(Type type, @Nullable RandomDimensionData obj, ConfigurationNode node) throws SerializationException {

    }
}
