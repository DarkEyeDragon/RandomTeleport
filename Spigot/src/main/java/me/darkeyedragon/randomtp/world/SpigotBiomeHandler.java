package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import org.bukkit.block.Biome;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpigotBiomeHandler implements RandomBiomeHandler {

    @Override
    public RandomBiome getBiome(String biomeName) {
        biomeName = biomeName.toUpperCase();
        return new SpigotBiome(Biome.valueOf(biomeName));
    }

    @Override
    public Set<RandomBiome> getBiomes(Pattern pattern) throws IllegalArgumentException {
        Set<RandomBiome> biomes = new HashSet<>();
        for (Biome biome : Biome.values()) {
            Matcher matcher = pattern.matcher(biome.name());
            if (matcher.matches()) {
                biomes.add(getBiome(matcher.group(0)));
            }
        }
        return biomes;
    }
}
