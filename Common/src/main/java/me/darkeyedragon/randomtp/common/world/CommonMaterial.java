package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.world.RandomMaterial;

public class CommonMaterial implements RandomMaterial {
    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isAir() {
        return false;
    }
}
