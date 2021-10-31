package me.darkeyedragon.randomtp.teleport;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;

public class SpigotCooldown implements RandomCooldown {

    private final long startTime;
    private final long cooldown;

    public SpigotCooldown(long startTime, long cooldown) {
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

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public long getRemainingTime() {
        if (isExpired()) {
            return -1;
        } else {
            return startTime + cooldown - System.currentTimeMillis();
        }
    }
}
