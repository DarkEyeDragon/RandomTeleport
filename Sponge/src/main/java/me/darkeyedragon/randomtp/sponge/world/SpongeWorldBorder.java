package me.darkeyedragon.randomtp.sponge.world;

import com.flowpowered.math.vector.Vector3d;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;
import me.darkeyedragon.randomtp.sponge.world.util.WorldUtil;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

public class SpongeWorldBorder implements RandomWorldBorder {
    private final World world;
    private final WorldBorder worldBorder;

    public SpongeWorldBorder(World world, WorldBorder worldBorder) {
        this.world = world;
        this.worldBorder = worldBorder;
    }

    @Override
    public RandomLocation getCenter() {
        Vector3d center = worldBorder.getCenter();
        return new CommonLocation(WorldUtil.toRandomWorld(world), center.getX(), center.getY(), center.getZ());
    }

    @Override
    public double getSize() {
        return worldBorder.getDiameter();
    }

    @Override
    public int getWarningDistance() {
        return worldBorder.getWarningDistance();
    }
}
