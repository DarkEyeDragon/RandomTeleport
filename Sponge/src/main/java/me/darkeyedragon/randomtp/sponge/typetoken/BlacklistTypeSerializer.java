package me.darkeyedragon.randomtp.sponge.typetoken;

import com.google.common.reflect.TypeToken;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.DimensionData;
import me.darkeyedragon.randomtp.common.config.Blacklist;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlacklistTypeSerializer implements TypeSerializer<Blacklist> {
    //TODO implement
    @Override
    public @Nullable Blacklist deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        Map<Dimension, DimensionData> data = new HashMap<>();
        value.getChildrenMap().forEach((o, configurationNode) -> {
            DimensionData dimensionData = new DimensionData();
            try {
                List<String> blackListEntry = configurationNode.getList(TypeToken.of(String.class));

            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
            data.put(Dimension.valueOf((String)o), dimensionData);
        });
        return null;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Blacklist obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }
}
