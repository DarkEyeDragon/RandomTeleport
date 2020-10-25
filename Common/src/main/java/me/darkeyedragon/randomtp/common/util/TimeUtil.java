package me.darkeyedragon.randomtp.common.util;

public class TimeUtil {
    /**
     * provides a String representation of the given time
     *
     * @return {@code millis} in hh:mm:ss format
     */
    public static CustomTime formatTime(long millis) {
        long secs = millis / 1000;
        return new CustomTime(secs, secs / 60, secs / 3600);
    }

    /**
     * @param message the message to format. Valid suffixes are s and m
     * @return A duration in milliseconds
     * @throws NumberFormatException Thrown when non of the formats match
     */
    public static long stringToLong(String message) throws NumberFormatException {
        if(message == null || message.isEmpty())
            throw new IllegalArgumentException("String can not be null or empty");
        String suffix = message.substring(message.length() - 1);
        String timeStr = message.replace(suffix, "");
        long time = Integer.parseInt(timeStr);
        switch (suffix) {
            case "s":
                time *= 1000;
                break;
            case "m":
                time *= 60000;
                break;
            default:
                throw new NumberFormatException("Not a valid format");
        }
        return time;
    }
    public static long stringToTicks(String message){
        if(message == null || message.isEmpty())
            throw new IllegalArgumentException("String can not be null or empty");
        String suffix = message.substring(message.length() - 1);
        String timeStr = message.replace(suffix, "");
        long time = Integer.parseInt(timeStr);
        switch (suffix) {
            case "s":
                time *= 20;
                break;
            case "m":
                time *= 120;
                break;
            default:
                throw new NumberFormatException("Not a valid format");
        }
        return time;
    }
    public static String toFormattedString(String message, CustomTime duration){
        String messagePart = message.replaceAll("%hp", duration.getHours()+"");
        messagePart = messagePart.replaceAll("%mp", duration.getMinutes()+"");
        messagePart = messagePart.replaceAll("%sp", duration.getSeconds()+"");
        messagePart = messagePart.replaceAll("%m", duration.getTotalMinutes()+"");
        messagePart = messagePart.replaceAll("%h", duration.getTotalHours()+"");
        message = messagePart.replaceAll("%s", duration.getTotalSeconds()+"");
        return message;
    }
}
