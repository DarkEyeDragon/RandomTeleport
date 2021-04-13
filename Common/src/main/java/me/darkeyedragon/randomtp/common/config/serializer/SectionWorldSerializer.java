package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.common.config.datatype.World;
import me.darkeyedragon.randomtp.common.config.datatype.WorldDetail;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionWorld;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SectionWorldSerializer implements TypeSerializer<SectionWorld> {

    public static final SectionWorldSerializer INSTANCE = new SectionWorldSerializer();

    @Override
    public SectionWorld deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Map<String, WorldDetail> worlds = node.get(new TypeToken<Map<String, WorldDetail>>() {
        });
        if (worlds != null) {
            Set<ConfigWorld> worldSet = worlds.entrySet().stream().map(stringWorldDetailEntry -> new World(stringWorldDetailEntry.getKey(), stringWorldDetailEntry.getValue())).collect(Collectors.toSet());
            return new CommonSectionWorld(worldSet);
        }
        return null;
    }

    @Override
    public void serialize(Type type, @Nullable SectionWorld obj, ConfigurationNode node) throws SerializationException {

    }
}
