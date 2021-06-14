package me.darkeyedragon.randomtp.common.config.serializer;

import io.leangen.geantyref.TypeToken;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import me.darkeyedragon.randomtp.api.config.section.SectionBlacklist;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomMaterialHandler;
import me.darkeyedragon.randomtp.common.config.datatype.Blacklist;
import me.darkeyedragon.randomtp.common.config.datatype.DimensionData;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionBlacklist;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.common.world.CommonBlockType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SectionBlacklistSerializer implements TypeSerializer<SectionBlacklist> {

    private final RandomTeleportPluginImpl impl;

    public SectionBlacklistSerializer(RandomTeleportPluginImpl impl) {
        this.impl = impl;
    }

    @Override
    public SectionBlacklist deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Map<Dimension, Map<String, List<String>>> dimensionDataMap = node.get(new TypeToken<Map<Dimension, Map<String, List<String>>>>() {
        });
        Map<Dimension, RandomDimensionData> data = new HashMap<>();
        RandomMaterialHandler materialHandler = impl.getMaterialHandler();
        if (dimensionDataMap == null) throw new SerializationException();
        dimensionDataMap.forEach((dimension, stringListMap) -> {
            DimensionData dimensionData = new DimensionData();
            List<String> blockStrList = stringListMap.get("block");
            Set<RandomBlockType> randomBlockTypes = new HashSet<>();
            if (blockStrList != null) {
                for (String s : blockStrList) {
                    if (s.startsWith("$")) {
                        randomBlockTypes.addAll(materialHandler.getFromTag(s.substring(1)).stream().map(CommonBlockType::new).collect(Collectors.toSet()));
                    } else {
                        Pattern pattern = Pattern.compile(s);
                        randomBlockTypes.addAll(materialHandler.getMaterials(pattern).stream().map(CommonBlockType::new).collect(Collectors.toSet()));
                    }
                }
                dimensionData.addAllBlockTypes(randomBlockTypes);
            }
            if (dimension == Dimension.GLOBAL) {
                data.put(dimension, dimensionData);
                return;
            }
            RandomBiomeHandler biomeHandler = impl.getWorldHandler().getBiomeHandler();
            Set<RandomBiome> randomBiomes = new HashSet<>();
            List<String> biomeStrList = stringListMap.get("biome");
            for (String s : biomeStrList) {
                Pattern pattern = Pattern.compile(s);
                randomBiomes.addAll(biomeHandler.getBiomes(pattern));
            }
            dimensionData.addAllBiomes(randomBiomes);
            data.put(dimension, dimensionData);
        });
        return new CommonSectionBlacklist(new Blacklist(data));
    }

    @Override
    public void serialize(Type type, @Nullable SectionBlacklist obj, ConfigurationNode node) throws SerializationException {

    }
}
