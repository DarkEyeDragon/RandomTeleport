package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Material;

public class SpigotBlockType implements RandomBlockType {

    private final Material material;

    public SpigotBlockType(Material material) {
        this.material = material;
    }

    @Override
    public RandomMaterial getType() {
        return new SpigotMaterial(material);
    }

    @Override
    public int hashCode() {
        return material.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpigotBlockType) {
            return ((SpigotBlockType) obj).material == material;
        }
        return false;
    }
}
