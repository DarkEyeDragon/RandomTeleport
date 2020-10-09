package me.darkeyedragon.randomtp.api.event;

public abstract class AbstractCancelable implements Cancelable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
