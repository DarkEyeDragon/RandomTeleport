package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;

import java.util.UUID;

public class BasicCooldown implements RandomCooldown {

    private final long startTime;
    private final long cooldown;
    private final UUID uuid;

    /**
     * @param uuid      the player's unique id
     * @param startTime the cooldown start time
     * @param cooldown  the duration in milliseconds
     */
    public BasicCooldown(UUID uuid, long startTime, long cooldown) {
        this.uuid = uuid;
        this.startTime = startTime;
        this.cooldown = cooldown;
    }

    @Override
    public boolean isExpired() {
        long remaining = startTime + cooldown - System.currentTimeMillis();
        return remaining < 0;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the cooldown in ticks
     */
    @Override
    public long getCooldown() {
        return cooldown;
    }

    /**
     * @return -1 if time is expired, otherwise the amount of time remaining in milliseconds.
     */
    @Override
    public long getRemainingTime() {
        if (isExpired()) {
            return -1;
        } else {
            return (startTime + cooldown - System.currentTimeMillis());
        }
    }

    public UUID getUuid() {
        return uuid;
    }
}
