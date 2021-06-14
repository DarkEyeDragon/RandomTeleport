package me.darkeyedragon.randomtp.api.config;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;

import java.util.Collection;
import java.util.Set;

public interface RandomDimensionData {
    Set<RandomBlockType> getBlockTypes();

    Set<RandomBiome> getBiomes();

    void addBiome(RandomBiome biome);

    void addAllBiomes(Collection<RandomBiome> biomes);

    void addBlockType(RandomBlockType blockType);

    void addAllBlockTypes(Collection<RandomBlockType> blockTypes);
}
