package me.darkeyedragon.randomtp.api.world.block;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface RandomBlock {
    RandomLocation getLocation();
    boolean isPassable();

    boolean isLiquid();

    RandomBlock getRelative(BlockFace blockFace);

    boolean isEmpty();

}
