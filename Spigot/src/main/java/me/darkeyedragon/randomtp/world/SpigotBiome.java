package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import org.bukkit.block.Biome;

public class SpigotBiome implements RandomBiome {

    protected final Biome biome;

    public SpigotBiome(Biome biome) {
        this.biome = biome;
    }
}
