package me.darkeyedragon.randomtp.common.world.location;

import me.darkeyedragon.randomtp.api.util.RandomVector;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommonLocation implements RandomLocation {

    private final RandomWorld randomWorld;
    private final float yaw;
    private final float pitch;
    private double x;
    private double y;
    private double z;

    public CommonLocation(RandomWorld world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public CommonLocation(RandomWorld world, double x, double y, double z, float yaw, float pitch) {
        this.randomWorld = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getBlockX() {
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
    }

    public RandomWorld getWorld() {
        return randomWorld;
    }

    public RandomBlock getBlock() {
        return randomWorld.getBlockAt(this);
    }

    @Override
    public RandomLocation clone() {
        return new CommonLocation(randomWorld, x, y, z);
    }

    public RandomLocation add(double x, double y, double z) {
        return new CommonLocation(this.randomWorld, this.x + x, this.y + y, this.z + z);
    }

    @Override
    public RandomLocation add(RandomVector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    /**
     * Gets a unit-vector pointing in the direction that this Location is
     * facing.
     *
     * @return a vector pointing the direction of this location's {@link
     * #getPitch() pitch} and {@link #getYaw() yaw}
     */
    @NotNull
    public Vector getDirection() {
        Vector vector = new Vector();

        double rotX = this.yaw;
        double rotY = this.pitch;

        vector.setY(-Math.sin(Math.toRadians(rotY)));

        double xz = Math.cos(Math.toRadians(rotY));

        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof RandomLocation) {
            RandomLocation otherLoc = (RandomLocation) obj;
            return this.randomWorld.equals(otherLoc.getWorld())
                    && this.x == otherLoc.getX()
                    && this.y == otherLoc.getY()
                    && this.z == otherLoc.getZ();
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(randomWorld, x, y, z);
    }
}
