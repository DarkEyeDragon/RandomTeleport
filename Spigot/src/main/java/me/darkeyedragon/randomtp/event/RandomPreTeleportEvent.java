package me.darkeyedragon.randomtp.event;

import me.darkeyedragon.randomtp.teleport.TeleportPropertySpigot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RandomPreTeleportEvent extends Event implements Cancellable {

    private final Player player;
    private final TeleportPropertySpigot property;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;

    /**
     * Event that's triggered before a player is teleported. This is before cooldowns and validation.
     *
     * @param player   The player about to be teleported
     * @param property The properties associated with the teleport.
     */
    public RandomPreTeleportEvent(Player player, TeleportPropertySpigot property) {
        this.player = player;
        this.property = property;
    }

    public Player getPlayer() {
        return player;
    }

    public TeleportPropertySpigot getProperty() {
        return property;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
