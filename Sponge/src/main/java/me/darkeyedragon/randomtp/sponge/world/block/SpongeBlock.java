package me.darkeyedragon.randomtp.sponge.world.block;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomBlockType;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.CommonBlockType;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;
import me.darkeyedragon.randomtp.sponge.world.SpongeBiome;
import me.darkeyedragon.randomtp.sponge.world.SpongeWorld;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.PassableProperty;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Objects;
import java.util.Optional;

public class SpongeBlock implements RandomBlock {

    private final World world;
    private final Location<World> location;

    public SpongeBlock(Location<World> location) {
        this.world = location.getExtent();
        this.location = location;
    }

    @Override
    public RandomLocation getLocation() {
        return new CommonLocation(new SpongeWorld(world), location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean isPassable() {
        Optional<PassableProperty> property = location.getProperty(PassableProperty.class);
        return property.map(passableProperty -> Boolean.TRUE.equals(passableProperty.getValue())).orElse(false);
    }

    @Override
    public boolean isLiquid() {
        Optional<MatterProperty> property = location.getProperty(MatterProperty.class);
        return property.map(matterProperty -> Objects.equals(matterProperty.getValue(), MatterProperty.Matter.LIQUID)).orElse(false);
    }

    @Override
    public RandomBlock getRelative(BlockFace blockFace) {
        return new SpongeBlock(location.getRelative(Direction.valueOf(blockFace.name().replace("_", ""))));
    }

    @Override
    public boolean isEmpty() {
        return location.getExtent().getBlock(location.getBlockPosition()).getType() == BlockTypes.AIR;
    }

    @Override
    public RandomBiome getBiome() {
        return new SpongeBiome(location.getBiome());
    }

    @Override
    public RandomBlockType getBlockType() {
        return new CommonBlockType(new SpongeMaterial(world.getBlockType(location.getBlockPosition())));
    }
}
