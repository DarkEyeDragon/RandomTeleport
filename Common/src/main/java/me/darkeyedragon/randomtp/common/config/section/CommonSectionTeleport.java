package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionTeleport implements SectionTeleport {

    private long cooldown;
    private long delay;
    private boolean cancelOnMove;
    private long deathTimer;
    private RandomParticle particle;
    private boolean useDefaultWorld;
    private String defaultWorld;
    private boolean useAsyncChunkTeleport;

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
    public RandomParticle getParticle() {
        return particle;
    }

    @Override
    public void setParticle(RandomParticle teleportParticle) {
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

    @Override
    public boolean isUseAsyncChunkTeleport() {
        return useAsyncChunkTeleport;
    }

    @Override
    public void setUseAsyncChunkTeleport(boolean useAsyncChunkTeleport) {
        this.useAsyncChunkTeleport = useAsyncChunkTeleport;
    }
}
