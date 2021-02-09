package me.darkeyedragon.randomtp.event;

import me.darkeyedragon.randomtp.common.teleport.CommonTeleportProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RandomTeleportCompletedEvent extends Event {

    private final Player player;
    private final CommonTeleportProperty property;
    private static final HandlerList handlers = new HandlerList();

    public RandomTeleportCompletedEvent(Player player, CommonTeleportProperty property) {
        this.player = player;
        this.property = property;
    }

    public Player getPlayer() {
        return player;
    }

    public CommonTeleportProperty getProperty() {
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
