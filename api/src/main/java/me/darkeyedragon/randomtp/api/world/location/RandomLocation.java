package me.darkeyedragon.randomtp.api.world.location;

import java.util.UUID;

public class RandomLocation {

    private final UUID worldUuid;
    private final int x;
    private final int y;
    private final int z;

    public RandomLocation(UUID world, int x, int y, int z) {
        this.worldUuid = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getWorldUuid() {
        return worldUuid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
