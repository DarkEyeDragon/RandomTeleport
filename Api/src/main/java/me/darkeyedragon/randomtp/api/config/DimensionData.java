package me.darkeyedragon.randomtp.api.config;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DimensionData {
    private final Set<RandomBlockType> blockTypes;
    private final Set<RandomBiome> biomes;

    public DimensionData() {
        blockTypes = new HashSet<>();
        biomes = new HashSet<>();
    }

    public Set<RandomBlockType> getBlockTypes() {
        return blockTypes;
    }

    public Set<RandomBiome> getBiomes() {
        return biomes;
    }

    public void addBiome(RandomBiome biome) {
        biomes.add(biome);
    }

    public void addAllBiomes(Collection<RandomBiome> biomes) {
        for (RandomBiome biome : biomes) {
            addBiome(biome);
        }
    }

    public void addBlockType(RandomBlockType blockType) {
        blockTypes.add(blockType);
    }

    public void addAllBlockTypes(Collection<RandomBlockType> blockTypes) {
        for (RandomBlockType blockType : blockTypes) {
            addBlockType(blockType);
        }
    }
}
