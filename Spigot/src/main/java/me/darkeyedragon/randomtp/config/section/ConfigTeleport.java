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
    private boolean useDefaultWorld;
    private String defaultWorld;

    public ConfigTeleport(long cooldown, long delay, boolean cancelOnMove, long deathTimer, TeleportParticle<Particle> particle, boolean useDefaultWorld, String defaultWorld) {
        this.cooldown = cooldown;
        this.delay = delay;
        this.cancelOnMove = cancelOnMove;
        this.deathTimer = deathTimer;
        this.particle = particle;
        this.useDefaultWorld = useDefaultWorld;
        this.defaultWorld = defaultWorld;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public boolean isCancelOnMove() {
        return cancelOnMove;
    }

    @Override
    public void setCancelOnMove(boolean cancelOnMove) {
        this.cancelOnMove = cancelOnMove;
    }

    @Override
    public long getDeathTimer() {
        return deathTimer;
    }

    @Override
    public void setDeathTimer(long deathTimer) {
        this.deathTimer = deathTimer;
    }

    @Override
    public TeleportParticle<Particle> getParticle() {
        return particle;
    }

    @Override
    public void setParticle(TeleportParticle teleportParticle) {
        this.particle = teleportParticle;
    }

    @Override
    public boolean getUseDefaultWorld() {
        return useDefaultWorld;
    }

    @Override
    public void setUseDefaultWorld(boolean useDefaultWorld) {
        this.useDefaultWorld = useDefaultWorld;
    }

    @Override
    public String getDefaultWorld() {
        return defaultWorld;
    }

    @Override
    public void setDefaultWorld(String defaultWorld) {
        this.defaultWorld = defaultWorld;
    }
}
