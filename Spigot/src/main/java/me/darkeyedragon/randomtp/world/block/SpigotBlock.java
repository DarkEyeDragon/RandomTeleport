package me.darkeyedragon.randomtp.world.block;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;
import me.darkeyedragon.randomtp.world.SpigotBiome;
import me.darkeyedragon.randomtp.world.SpigotBlockType;
import me.darkeyedragon.randomtp.world.SpigotWorld;
import org.bukkit.block.Block;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpigotBlock implements RandomBlock {

    final Block block;

    public SpigotBlock(Block block) {
        this.block = block;
    }

    @Override
    public RandomLocation getLocation() {
        return new CommonLocation(new SpigotWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
    }

    @Override
    public boolean isPassable() {
        return block.isPassable();
    }

    @Override
    public boolean isLiquid() {
        return block.isLiquid();
    }

    @Override
    public RandomBlock getRelative(BlockFace blockFace) {
        return new SpigotBlock(block.getRelative(org.bukkit.block.BlockFace.valueOf(blockFace.name())));
    }

    @Override
    public boolean isEmpty() {
        return block.isEmpty();
    }

    @Override
    public RandomBiome getBiome() {
        return new SpigotBiome(block.getBiome());
    }

    @Override
    public RandomBlockType getBlockType() {
        return new SpigotBlockType(block.getType());
    }

    @Override
    public String toString() {
        return "SpigotBlock{" +
                "block=" + block +
                "world=" + block.getWorld() +
                '}';
    }
}
