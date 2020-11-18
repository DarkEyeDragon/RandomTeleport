package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;
import org.bukkit.Particle;

public class ConfigTeleport implements SectionTeleport {


    private long cooldown;
    private long delay;
    private boolean cancelOnMove;
    private long deathTimer;
    private TeleportParticle<Particle> particle;

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

    public ConfigTeleport particle(TeleportParticle<Particle> particle){
        this.particle = particle;
        return this;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public boolean isCancelOnMove() {
        return cancelOnMove;
    }

    @Override
    public long getDeathTimer() {
        return deathTimer;
    }

    @Override
    public TeleportParticle<Particle> getParticle() {
        return particle;
    }
}
