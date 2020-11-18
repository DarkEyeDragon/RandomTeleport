package me.darkeyedragon.randomtp.api.config;

import java.util.Map;

public interface RandomBlacklist {
    Map<Dimension, DimensionData> getDimensions();

    DimensionData getDimensionData(Dimension dimension);

    void addDimensionData(Dimension dimension, DimensionData dimensionData);
}
