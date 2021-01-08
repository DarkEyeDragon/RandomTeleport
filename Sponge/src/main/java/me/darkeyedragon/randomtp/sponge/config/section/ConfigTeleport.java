package me.darkeyedragon.randomtp.sponge.config.section;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigTeleport implements SectionTeleport {

    @Setting
    private long cooldown;
    @Setting
    private long delay;
    @Setting
    private boolean cancelOnMove;
    @Setting
    private long deathTimer;
    private boolean useDefaultWorld;
    private String defaultWorld;

    TeleportParticle<Particle> teleportParticle;

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
    public TeleportParticle getParticle() {
        return teleportParticle;
    }

    @Override
    public void setParticle(TeleportParticle teleportParticle) {
        this.teleportParticle = teleportParticle;
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
        return null;
    }

    @Override
    public void setDefaultWorld(String defaultWorld) {
        this.defaultWorld = defaultWorld;
    }

}
