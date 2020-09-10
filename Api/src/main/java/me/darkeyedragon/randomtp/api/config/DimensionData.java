package me.darkeyedragon.randomtp.api.config;

import me.darkeyedragon.randomtp.api.world.Biome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DimensionData<T> {
    private final Set<RandomBlockType<T>> blockTypes;
    private final Set<Biome> biomes;

    public DimensionData() {
        blockTypes = new HashSet<>();
        biomes = new HashSet<>();
    }

    public Set<RandomBlockType<T>> getBlockTypes() {
        return blockTypes;
    }

    public Set<Biome> getBiomes() {
        return biomes;
    }

    public void addBiome(Biome biome) {
        biomes.add(biome);
    }

    public void addAllBiomes(Collection<Biome> biomes) {
        for (Biome biome : biomes) {
            addBiome(biome);
        }
    }

    public void addBlockType(RandomBlockType<T> blockType) {
        blockTypes.add(blockType);
    }

    public void addAllBlockTypes(Collection<RandomBlockType<T>> blockTypes) {
        for (RandomBlockType<T> blockType : blockTypes) {
            addBlockType(blockType);
        }
    }
}
