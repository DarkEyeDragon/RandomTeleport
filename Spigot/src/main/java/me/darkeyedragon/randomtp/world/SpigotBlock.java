package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import org.bukkit.block.Block;

public class SpigotBlock implements RandomBlock {

    final Block block;

    public SpigotBlock(Block block) {
        this.block = block;
    }

    @Override
    public RandomLocation getLocation() {
        return new RandomLocation(new SpigotWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
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
}
