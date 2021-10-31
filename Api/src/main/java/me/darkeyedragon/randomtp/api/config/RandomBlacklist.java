package me.darkeyedragon.randomtp.api.config;

import java.util.Map;

public interface RandomBlacklist {
    Map<Dimension, RandomDimensionData> getDimensions();

    RandomDimensionData getDimensionData(Dimension dimension);

    void addDimensionData(Dimension dimension, RandomDimensionData dimensionData);
}
