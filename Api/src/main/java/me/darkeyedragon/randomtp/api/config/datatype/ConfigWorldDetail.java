package me.darkeyedragon.randomtp.api.config.datatype;

import me.darkeyedragon.randomtp.api.eco.EcoType;

public interface ConfigWorldDetail {
    boolean isUseWorldborder();

    boolean isNeedsWorldPermission();

    ConfigWorldborder getConfigWorldborder();

    double getPrice();

    EcoType getEcoType();
}
