package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.teleport.RandomParticle;

public interface SectionTeleport {
    long getCooldown();
    void setCooldown(long cooldown);
    long getDelay();
    void setDelay(long delay);
    boolean isCancelOnMove();
    void setCancelOnMove(boolean cancelOnMove);
    long getDeathTimer();

    void setDeathTimer(long deathTimer);

    RandomParticle<?> getParticle();

    void setParticle(RandomParticle<?> teleportParticle);

    boolean getUseDefaultWorld();
    void setUseDefaultWorld(boolean useDefaultWorld);
    String getDefaultWorld();
    void setDefaultWorld(String defaultWorld);

}
