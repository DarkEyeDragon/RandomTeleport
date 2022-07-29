package me.darkeyedragon.randomtp.api.world.location;

import com.sun.tools.javac.util.Pair;
import me.darkeyedragon.randomtp.api.util.RandomVector;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import org.jetbrains.annotations.NotNull;

public interface RandomLocation extends Cloneable {

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    RandomWorld getWorld();

    RandomBlock getBlock();

    RandomLocation clone();

    RandomLocation add(double x, double y, double z);

    RandomLocation add(RandomVector direction);

    /**
     * Gets a unit-vector pointing in the direction that this Location is
     * facing.
     *
     * @return a vector pointing the direction of this location's yaw and pitch
     */
    @NotNull
    RandomVector getDirection();

    /**
     * @return a {@link Pair} of chunk coordinates. First being X and second being Z
     */
    ChunkLocation toChunkLocation();

}
