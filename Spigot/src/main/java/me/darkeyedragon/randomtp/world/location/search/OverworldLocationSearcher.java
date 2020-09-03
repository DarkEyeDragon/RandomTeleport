package me.darkeyedragon.randomtp.world.location.search;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.location.search.BaseLocationSearcher;
import me.darkeyedragon.randomtp.world.SpigotBiome;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class OverworldLocationSearcher extends BaseLocationSearcher {
    /**
     * A simple utility class to help with {@link Location}
     *
     * @param plugin the {@link RandomTeleport} instance
     */
    public OverworldLocationSearcher(RandomTeleport plugin) {
        super(plugin);
        blacklistMaterial.add(new SpigotMaterial(Material.LAVA));
        blacklistMaterial.add(new SpigotMaterial(Material.CACTUS));
        blacklistMaterial.add(new SpigotMaterial(Material.FIRE));
        blacklistMaterial.add(new SpigotMaterial(Material.MAGMA_BLOCK));
        blacklistMaterial.add(new SpigotMaterial(Material.TRIPWIRE));
        blacklistMaterial.add(new SpigotMaterial(Material.ACACIA_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.BIRCH_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.JUNGLE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.OAK_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.SPRUCE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.STONE_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.DARK_OAK_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
        blacklistMaterial.add(new SpigotMaterial(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));

        //Illegal biomes
        for (Biome biome : Biome.values()) {
            if (biome.toString().toLowerCase().contains(OCEAN)) {
                blacklistBiome.add(new SpigotBiome(biome));
            }
        }
    }
}
