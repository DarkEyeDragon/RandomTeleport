package me.darkeyedragon.randomtp.api.config.section;

public interface SectionTeleport {
    long getCooldown();
    long getDelay();
    boolean isCancelOnMove();
    long getDeathTimer();
}
