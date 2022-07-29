package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.api.scheduler.TaskIdentifier;

public record SpigotTaskIdentifier(int id) implements TaskIdentifier<Integer> {

    @Override
    public Integer getIdentifier() {
        return id;
    }
}
