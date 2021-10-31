package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.common.world.CommonBlockType;
import me.darkeyedragon.randomtp.world.block.SpigotMaterial;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpigotBlockType extends CommonBlockType {

    private final Material material;

    public SpigotBlockType(Material material) {
        super(new SpigotMaterial(material));
        this.material = material;
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
