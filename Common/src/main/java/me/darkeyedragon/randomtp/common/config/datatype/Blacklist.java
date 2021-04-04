package me.darkeyedragon.randomtp.common.config.datatype;

import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.RandomBlacklist;
import me.darkeyedragon.randomtp.api.config.RandomDimensionData;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class Blacklist implements RandomBlacklist {

    private final Map<Dimension, RandomDimensionData> dimensions;

    public Blacklist(Map<Dimension, RandomDimensionData> dimensions) {
        this.dimensions = dimensions;
    }

    public Blacklist() {
        this(new HashMap<>());
    }

    public Map<Dimension, RandomDimensionData> getDimensions() {
        return dimensions;
    }

    public RandomDimensionData getDimensionData(Dimension dimension) {
        return dimensions.get(dimension);
    }

    @Override
    public void addDimensionData(Dimension dimension, RandomDimensionData dimensionData) {
        dimensions.put(dimension, dimensionData);
    }
}
