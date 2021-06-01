package me.darkeyedragon.randomtp.common.util;

public class CustomTime {
    private final long totalSeconds;
    private final long totalMinutes;
    private final long totalHours;

    private final long seconds;
    private final long minutes;
    private final long hours;

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

    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        if (totalHours > 0) {
            builder.append(hours).append("h ");
        }
        if (totalMinutes > 0) {
            builder.append(minutes).append("m ");
        }
        builder.append(seconds).append("s");
        return builder.toString();
    }
}