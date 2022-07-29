package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import org.spongepowered.api.world.biome.BiomeType;

public class SpongeBiome implements RandomBiome {
    private final BiomeType biome;

    public SpongeBiome(BiomeType biome) {
        this.biome = biome;
    }

    @Override
    public boolean equals(Object obj) {
        if (biome == obj) return true;
        if (obj instanceof BiomeType) {
            return biome.getName().equals(((BiomeType) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return biome.hashCode();
    }
}
