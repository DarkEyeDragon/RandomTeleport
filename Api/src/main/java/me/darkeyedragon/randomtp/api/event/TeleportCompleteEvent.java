package me.darkeyedragon.randomtp.api.event;

import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;

public interface TeleportCompleteEvent {
    boolean isSuccessful();

    boolean isCanceled();

    void setCanceled(boolean cancel);

    TeleportProperty getTeleportProperty();
}
