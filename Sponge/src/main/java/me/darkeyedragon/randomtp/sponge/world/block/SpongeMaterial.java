package me.darkeyedragon.randomtp.sponge.world.block;

import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.AbstractProperty;
import org.spongepowered.api.data.property.block.PassableProperty;

public class SpongeMaterial implements RandomMaterial {

    private final BlockType blockType;

    public SpongeMaterial(BlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public boolean isSolid() {
        return !blockType.getProperty(PassableProperty.class).map(AbstractProperty::getValue).orElse(true);
    }

    @Override
    public String getName() {
        return blockType.getName();
    }

    @Override
    public boolean isAir() {
        return blockType == BlockTypes.AIR;
    }
}
