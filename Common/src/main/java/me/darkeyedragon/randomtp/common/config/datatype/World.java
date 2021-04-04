package me.darkeyedragon.randomtp.common.config.datatype;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class World {

    private boolean useWorldborder;
    private boolean needsWorldPermission;
    private Worldborder worldborder;
    private double price;

    public boolean isUseWorldborder() {
        return useWorldborder;
    }

    public boolean isNeedsWorldPermission() {
        return needsWorldPermission;
    }

    public Worldborder getWorldborder() {
        return worldborder;
    }

    public double getPrice() {
        return price;
    }
}
