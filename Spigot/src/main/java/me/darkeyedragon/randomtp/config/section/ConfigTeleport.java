package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;

public class ConfigTeleport implements SectionTeleport {


    private long cooldown;
    private long delay;
    private boolean cancelOnMove;
    private long deathTimer;

    public ConfigTeleport cooldown(long cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public ConfigTeleport delay(long delay) {
        this.delay = delay;
        return this;
    }

    public ConfigTeleport deathTimer(long deathTimer) {
        this.deathTimer = deathTimer;
        return this;
    }

    public ConfigTeleport cancelOnMove(boolean cancelOnMove) {
        this.cancelOnMove = cancelOnMove;
        return this;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isCancelOnMove() {
        return cancelOnMove;
    }

    public long getDeathTimer() {
        return deathTimer;
    }
}
