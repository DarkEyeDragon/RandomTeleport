package me.darkeyedragon.randomtp.api.world;

import java.util.Set;
import java.util.regex.Pattern;

public interface RandomBiomeHandler {

    RandomBiome getBiome(String materialName);

    Set<RandomBiome> getBiomes(Pattern pattern) throws IllegalArgumentException;

}
