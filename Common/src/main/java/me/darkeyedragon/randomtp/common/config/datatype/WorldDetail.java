package me.darkeyedragon.randomtp.common.config.datatype;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorldDetail;
import me.darkeyedragon.randomtp.api.eco.EcoType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class WorldDetail implements ConfigWorldDetail {

    private boolean useWorldborder;
    private boolean needsWorldPermission;
    private WorldBorder worldborder;
    private double price;

    @Override
    public boolean isUseWorldborder() {
        return useWorldborder;
    }

    @Override
    public boolean isNeedsWorldPermission() {
        return needsWorldPermission;
    }

    public WorldBorder getConfigWorldborder() {
        return worldborder;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public EcoType getEcoType() {
        if (price == -1) return EcoType.NONE;
        if (price == 0) return EcoType.GLOBAL;
        else return EcoType.LOCAL;
    }

}
