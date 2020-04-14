package me.darkeyedragon.randomtp.util;

public class TimeUtil {
    /**
     * provides a String representation of the given time
     * @return {@code millis} in hh:mm:ss format
     */
    public static CustomTime formatTime(long millis) {
        long secs = millis / 1000;
        return new CustomTime(secs, secs/60, secs/3600);
    }
}
