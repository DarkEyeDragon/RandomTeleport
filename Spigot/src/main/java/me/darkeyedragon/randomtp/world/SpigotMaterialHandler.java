package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.api.world.RandomMaterialHandler;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpigotMaterialHandler implements RandomMaterialHandler {

    @Override
    public Set<RandomMaterial> getFromTag(String tagName) {
        Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
        Set<RandomMaterial> randomMaterials = new HashSet<>();

        for (Tag<Material> tag : tags) {
            if (tag.getKey().getKey().equalsIgnoreCase(tagName)) {
                for (Material value : tag.getValues()) {
                    randomMaterials.add(new SpigotMaterial(value));
                }
                break;
            }
        }
        return randomMaterials;
    }

    @Override
    public RandomMaterial getMaterial(String materialName) {
        materialName = materialName.toUpperCase();
        return new SpigotMaterial(Material.valueOf(materialName));
    }

    @Override
    public Set<RandomMaterial> getMaterials(Pattern pattern) throws IllegalArgumentException {
        Set<RandomMaterial> randomMaterials = new HashSet<>();
        for (Material material : Material.values()) {
            Matcher matcher = pattern.matcher(material.name());
            if (matcher.matches()) {
                randomMaterials.add(new SpigotMaterial(material));
            }
        }
        return randomMaterials;
    }
}
