package me.darkeyedragon.randomtp.event;

import me.darkeyedragon.randomtp.api.event.RandomEvent;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RandomLocationFoundEvent extends Event implements RandomEvent {
    private static final HandlerList handlers = new HandlerList();
    private final RandomLocation location;

    public RandomLocationFoundEvent(RandomLocation location) {
        this.location = location;
    }

    public RandomLocation getLocation() {
        return location;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }
}
