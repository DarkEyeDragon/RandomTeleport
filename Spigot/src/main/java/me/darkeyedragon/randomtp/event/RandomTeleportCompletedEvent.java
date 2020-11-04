package me.darkeyedragon.randomtp.event;

import me.darkeyedragon.randomtp.teleport.TeleportProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportCompletedEvent extends Event {

    private final Player player;
    private final TeleportProperty property;
    private static final HandlerList handlers = new HandlerList();

    public RandomTeleportCompletedEvent(Player player, TeleportProperty property) {
        this.player = player;
        this.property = property;
    }

    public Player getPlayer() {
        return player;
    }

    public TeleportProperty getProperty() {
        return property;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
