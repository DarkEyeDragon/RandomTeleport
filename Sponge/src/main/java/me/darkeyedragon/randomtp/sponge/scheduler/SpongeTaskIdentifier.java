package me.darkeyedragon.randomtp.sponge.scheduler;

import me.darkeyedragon.randomtp.api.scheduler.TaskIdentifier;

import java.util.UUID;

public class SpongeTaskIdentifier implements TaskIdentifier<UUID> {


    private final UUID uniqueId;

    public SpongeTaskIdentifier(UUID uniqueId) {

        this.uniqueId = uniqueId;
    }

    @Override
    public UUID getIdentifier() {
        return uniqueId;
    }
}
