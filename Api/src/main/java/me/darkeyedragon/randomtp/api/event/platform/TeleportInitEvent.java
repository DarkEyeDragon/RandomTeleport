package me.darkeyedragon.randomtp.api.event.platform;

import me.darkeyedragon.randomtp.api.event.AbstractCancelable;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;

public class TeleportInitEvent extends AbstractCancelable {

    private final TeleportProperty teleportProperty;

    public TeleportInitEvent(TeleportProperty teleportProperty) {
        this.teleportProperty = teleportProperty;
    }

    public TeleportProperty getTeleportProperty() {
        return teleportProperty;
    }
}
