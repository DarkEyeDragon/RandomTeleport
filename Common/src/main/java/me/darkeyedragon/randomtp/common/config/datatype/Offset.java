package me.darkeyedragon.randomtp.common.config.datatype;

import me.darkeyedragon.randomtp.api.world.location.RandomOffset;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Offset implements RandomOffset {
    private int x;
    private int z;

    public Offset(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public Offset() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
