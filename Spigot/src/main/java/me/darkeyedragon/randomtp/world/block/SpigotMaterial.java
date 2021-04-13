package me.darkeyedragon.randomtp.world.block;

import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpigotMaterial implements RandomMaterial {

    protected Material material;

    public SpigotMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean isSolid() {
        return material.isSolid();
    }

    @Override
    public String getName() {
        return material.name();
    }

    @Override
    public boolean isAir() {
        return material.isAir();
    }

}
