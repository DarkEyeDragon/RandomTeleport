package me.darkeyedragon.randomtp.common.config.datatype;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Worldborder {

    int radius;
    private Offset offset;

    public Offset getOffset() {
        return offset;
    }

    public int getRadius() {
        return radius;
    }
}
