package me.darkeyedragon.randomtp.common.util;

public class CustomTime {
    private long totalSeconds;
    private long totalMinutes;
    private long totalHours;

    private long seconds;
    private long minutes;
    private long hours;

    public CustomTime(long totalSeconds, long totalMinutes, long totalHours) {
        this.totalSeconds = totalSeconds;
        this.totalMinutes = totalMinutes;
        this.totalHours = totalHours;

        this.seconds = totalSeconds % 60;
        this.minutes = (totalSeconds % 3600) / 60;
        this.hours = totalSeconds / 3600;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public long getTotalHours() {
        return totalHours;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getHours() {
        return hours;
    }
}
