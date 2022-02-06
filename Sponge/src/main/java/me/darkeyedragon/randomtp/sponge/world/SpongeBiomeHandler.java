package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.biome.BiomeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpongeBiomeHandler implements RandomBiomeHandler {

    private static Collection<BiomeType> biomeTypes;


    private Collection<BiomeType> getBiomeTypes() {
        if (biomeTypes == null) {
            biomeTypes = Sponge.getRegistry().getAllOf(BiomeType.class);
        }
        return biomeTypes;
    }

    @Override
    public RandomBiome getBiome(String biomeName) {
        for (BiomeType biomeType : getBiomeTypes()) {
            if (biomeType.getName().equalsIgnoreCase(biomeName)) {
                new SpongeBiome(biomeType);
            }
        }
        return null;
    }

    @Override
    public Set<RandomBiome> getBiomes(Pattern pattern) throws IllegalArgumentException {
        Set<RandomBiome> materialHashSet = new HashSet<>();
        for (BiomeType blockType : getBiomeTypes()) {
            Matcher matcher = pattern.matcher(blockType.getName());
            if (matcher.matches()) {
                materialHashSet.add(new SpongeBiome(blockType));
            }
        }
        return materialHashSet;
    }
}
