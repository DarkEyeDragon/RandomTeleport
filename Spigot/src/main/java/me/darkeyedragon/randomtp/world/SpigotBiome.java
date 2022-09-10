package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import org.bukkit.block.Biome;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpigotBiome implements RandomBiome {

    protected final Biome biome;

    public SpigotBiome(Biome biome) {
        this.biome = biome;
    }

    @Override
    public boolean equals(Object obj) {
        if(biome == obj) return true;
        if (obj instanceof Biome) {
            return biome.name().equals(((Biome) obj).name());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return biome.hashCode();
    }

    @Override
    public String getName() {
        return biome.name();
    }
}
