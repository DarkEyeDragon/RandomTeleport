package me.darkeyedragon.randomtp.api.event;

public interface Cancelable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
