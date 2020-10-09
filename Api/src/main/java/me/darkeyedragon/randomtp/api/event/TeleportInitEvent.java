package me.darkeyedragon.randomtp.api.event;

import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;

public interface TeleportInitEvent {
    void setCanceled();

    boolean isCanceled();

    TeleportProperty getTeleportProperty();
}
