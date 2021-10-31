package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonBlockType implements RandomBlockType {

    private final RandomMaterial randomMaterial;

    public CommonBlockType(RandomMaterial randomMaterial) {
        this.randomMaterial = randomMaterial;
    }

    @Override
    public RandomMaterial getType() {
        return randomMaterial;
    }
}
