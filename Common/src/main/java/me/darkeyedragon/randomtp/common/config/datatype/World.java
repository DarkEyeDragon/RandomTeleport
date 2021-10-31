package me.darkeyedragon.randomtp.common.config.datatype;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorldborder;
import me.darkeyedragon.randomtp.api.eco.EcoType;

public class World implements ConfigWorld {

    private final String name;

    private final WorldDetail worldDetail;

    public World(String name, WorldDetail worldDetail) {
        this.name = name;
        this.worldDetail = worldDetail;
    }

    public boolean isUseWorldborder() {
        return worldDetail.isUseWorldborder();
    }

    public boolean isNeedsWorldPermission() {
        return worldDetail.isNeedsWorldPermission();
    }

    public ConfigWorldborder getConfigWorldborder() {
        return worldDetail.getConfigWorldborder();
    }

    public double getPrice() {
        return worldDetail.getPrice();
    }

    @Override
    public EcoType getEcoType() {
        return worldDetail.getEcoType();
    }

    public String getName() {
        return name;
    }

    public WorldDetail getWorldDetail() {
        return worldDetail;
    }
}
