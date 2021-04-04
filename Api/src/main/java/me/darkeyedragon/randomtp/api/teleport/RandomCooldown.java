package me.darkeyedragon.randomtp.api.teleport;

public interface RandomCooldown {

    /**
     * @return true if the timestamp minus current time is smaller than 0
     */
    boolean isExpired();

    /**
     * @return the timestamp that's been set
     */
    long getStartTime();

    /**
     * @return the cooldown time. use {@link #getRemainingTime()} to get the remaining time.
     */
    long getCooldown();

    /**
     * @return the remaining time on the cooldown
     */
    long getRemainingTime();
}
