package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionBlacklist;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SectionBlacklistSerializer implements TypeSerializer<SectionBlacklist> {

    public static final SectionBlacklistSerializer INSTANCE = new SectionBlacklistSerializer();

    @Override
    public SectionBlacklist deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return node.get(new TypeToken<CommonSectionBlacklist>() {
        });
    }

    @Override
    public void serialize(Type type, @Nullable SectionBlacklist obj, ConfigurationNode node) throws SerializationException {

    }
}
