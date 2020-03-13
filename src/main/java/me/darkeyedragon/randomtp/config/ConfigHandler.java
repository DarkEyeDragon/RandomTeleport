package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigHandler {

    private RandomTeleport plugin;
    private long cooldown = -1;

    public ConfigHandler(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    public String getInitMessage() {
        String message = plugin.getConfig().getString("message.initteleport");
        if (message != null)
            message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getTeleportMessage(){
        String message = plugin.getConfig().getString("message.teleport");
        if(message != null){
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }
    public String getBlacklistMessage(){
        String message = plugin.getConfig().getString("message.blacklisted");
        if(message != null){
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }
    public int getRadius(){
        return plugin.getConfig().getInt("teleport.radius");
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

    public int getStartX(){
        return plugin.getConfig().getInt("teleport.startX");
    }
    public int getStartZ(){
        return plugin.getConfig().getInt("teleport.startZ");
    }
    public Vector getStartLocation(){
        return new Vector((double)getStartX(), 0,(double)getStartZ());
    }

    private long formatCooldown() throws NumberFormatException {
        String message = plugin.getConfig().getString("teleport.cooldown");
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

    public List<World> getWorldsBlacklist(){
        var strings = plugin.getConfig().getStringList("blacklist.worlds");
        return strings.stream().map(Bukkit::getWorld).collect(Collectors.toList());
    }

    public long getCooldown() {
        if (cooldown == -1) {
            cooldown = formatCooldown();
        }
        return cooldown;
    }

    public List<String> getPlugins(){
        return plugin.getConfig().getStringList("plugins");
    }

    public boolean isWhitelist(){
        return plugin.getConfig().getBoolean("blacklist.isWhitelist");
    }
}
