package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.teleport.TeleportType;

public class BasicTeleportResponse implements TeleportResponse {

    private final TeleportType teleportType;

    public BasicTeleportResponse(TeleportType teleportType) {
        this.teleportType = teleportType;
    }

    @Override
    public TeleportType getTeleportType() {
        return teleportType;
    }
}
