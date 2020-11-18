package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;

public interface SectionTeleport {
    long getCooldown();
    long getDelay();
    boolean isCancelOnMove();
    long getDeathTimer();
    TeleportParticle getParticle();
}
