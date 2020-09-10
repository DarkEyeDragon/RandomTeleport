package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import org.bukkit.Material;

public class SpigotBlockType implements RandomBlockType<Material> {

    private final Material material;

    public SpigotBlockType(Material material) {
        this.material = material;
    }

    @Override
    public Material getType() {
        return material;
    }
}
