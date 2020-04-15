package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.util.CustomTime;
import me.darkeyedragon.randomtp.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.util.Vector;

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
    public String getDepletedQueueMessage() {
        String message = plugin.getConfig().getString("message.depleted_queue", "&6Locations queue depleted... Forcing generation of a new location");
        if(message != null){
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }
    public int getRadius(){
        return plugin.getConfig().getInt("size.radius");
    }

    public String getCountdownRemainingMessage(long remainingTime) {
        String message = plugin.getConfig().getString("message.countdown");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime duration = TimeUtil.formatTime(remainingTime);
            String messagePart = message.replaceAll("%hp", duration.getHours()+"");
            messagePart = messagePart.replaceAll("%mp", duration.getMinutes()+"");
            messagePart = messagePart.replaceAll("%sp", duration.getSeconds()+"");
            messagePart = messagePart.replaceAll("%m", duration.getTotalMinutes()+"");
            messagePart = messagePart.replaceAll("%h", duration.getTotalHours()+"");
            message = messagePart.replaceAll("%s", duration.getTotalSeconds()+"");
        }
        return message;
    }

    public int getOffsetX(){
        return plugin.getConfig().getInt("size.offsetX");
    }
    public int getOffsetZ(){
        return plugin.getConfig().getInt("size.offsetZ");
    }
    public Vector getStartLocation(){
        return new Vector((double) getOffsetX(), 0,(double)getOffsetZ());
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
        List<String> strings = plugin.getConfig().getStringList("blacklist.worlds");
        return strings.stream().map(Bukkit::getWorld).collect(Collectors.toList());
    }

    public int initDelay(){
        return plugin.getConfig().getInt("queue.init_delay", 90);
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

    public int getQueueSize(){
        return plugin.getConfig().getInt("queue.size", 10);
    }
    public boolean useWorldBorder(){
        return plugin.getConfig().getBoolean("size.use_worldborder");
    }
}
