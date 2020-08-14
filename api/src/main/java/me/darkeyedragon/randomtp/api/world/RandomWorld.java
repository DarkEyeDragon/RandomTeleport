package me.darkeyedragon.randomtp.api.world;

import java.util.UUID;

public class RandomWorld {

    final UUID worldUUID;

    public RandomWorld(UUID worldUUID) {
        this.worldUUID = worldUUID;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
}
