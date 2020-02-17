package me.darkeyedragon.randomtp.config;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.time.Duration;

public class ConfigHandler {

    private Plugin plugin;
    private long cooldown = -1;

    public ConfigHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getInitMessage() {
        String message = plugin.getConfig().getString("message.initteleport");
        if (message != null)
            message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getCountdownRemainingMessage(long remainingTime) {
        String message = plugin.getConfig().getString("message.countdown");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            Duration duration = Duration.ofMillis(remainingTime);
            String messagePart = message.replaceAll("%mp", duration.toMinutesPart()+"");
            messagePart = messagePart.replaceAll("%sp", duration.toSecondsPart()+"");
            messagePart = messagePart.replaceAll("%m", duration.toMinutes()+"");
            message = messagePart.replaceAll("%s", duration.toSeconds()+"");
        }
        return message;
    }

    private long formatCooldown() throws NumberFormatException {
        String message = plugin.getConfig().getString("cooldown");
        if (message != null) {
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
        throw new NumberFormatException("Not a valid format");
    }

    public long getCooldown() {
        if (cooldown == -1) {
            cooldown = formatCooldown();
        }
        return cooldown;
    }
}
