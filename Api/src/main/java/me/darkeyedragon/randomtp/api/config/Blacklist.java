package me.darkeyedragon.randomtp.api.config;

import java.util.HashMap;
import java.util.Map;

public class Blacklist<T> {

    private final Map<Dimension, DimensionData<T>> dimensions;


    public Blacklist() {
        dimensions = new HashMap<>();
    }

    public Map<Dimension, DimensionData<T>> getDimensions() {
        return dimensions;
    }

    public void addDimension(Dimension dimension, DimensionData<T> dimensionData) {
        dimensions.put(dimension, dimensionData);
    }
}
