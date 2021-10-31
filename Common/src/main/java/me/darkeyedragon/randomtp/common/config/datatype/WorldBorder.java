package me.darkeyedragon.randomtp.common.config.datatype;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorldborder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class WorldBorder implements ConfigWorldborder {

    protected int radius;
    protected Offset offset;

    public Offset getOffset() {
        return offset;
    }

    public int getRadius() {
        return radius;
    }
}
