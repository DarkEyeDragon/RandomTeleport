package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.section.SectionWorldHolder;
import me.darkeyedragon.randomtp.common.config.datatype.World;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionWorldHolder;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;

public class SectionWorldSerializer implements TypeSerializer<SectionWorldHolder> {

    public static final SectionWorldSerializer INSTANCE = new SectionWorldSerializer();

    @Override
    public SectionWorldHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Map<String, World> worlds = node.get(new TypeToken<Map<String, World>>() {
        });
        if (worlds != null)
            return new CommonSectionWorldHolder(new HashSet<>(worlds.values()));
        return null;
    }

    @Override
    public void serialize(Type type, @Nullable SectionWorldHolder obj, ConfigurationNode node) throws SerializationException {

    }
}
