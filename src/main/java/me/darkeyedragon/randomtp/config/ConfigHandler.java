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

    public String getInitTeleportMessage() {
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
            message = TimeUtil.toFormattedString(message, duration);
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
            return TimeUtil.stringToLong(message);
        }
        throw new NumberFormatException("Not a valid format");
    }

    public long getTeleportDelay(){
        String message = plugin.getConfig().getString("teleport.delay", "0s");
        if(message != null){
            return TimeUtil.stringToTicks(message);
        }
        throw new NumberFormatException("Not a valid number");
    }

    public List<World> getWorldsBlacklist(){
        List<String> strings = plugin.getConfig().getStringList("blacklist.worlds");
        return strings.stream().map(Bukkit::getWorld).collect(Collectors.toList());
    }

    public int getInitDelay(){
        return plugin.getConfig().getInt("queue.init_delay", 60);
    }
    public boolean getDebugShowQueuePopulation(){
        return plugin.getConfig().getBoolean("debug.show_queue_population", true);
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
        return plugin.getConfig().getInt("queue.size", 5);
    }
    public boolean useWorldBorder(){
        return plugin.getConfig().getBoolean("size.use_worldborder");
    }

    public String getCancelMessage() {
        String message = plugin.getConfig().getString("message.teleport_canceled", "&cYou moved! Teleportation canceled");
        if(message != null){
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }
    public String getInitTeleportDelay() {
        String message = plugin.getConfig().getString("message.teleport_delay", "&aYou will be teleported in &6%s seconds. Do not move");
        if(message != null){
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime time = TimeUtil.formatTime(getTeleportDelay()*50);
            message = TimeUtil.toFormattedString(message, time);
        }
        return message;
    }
    public boolean isCanceledOnMove(){
        return plugin.getConfig().getBoolean("teleport.cancel_on_move", false);
    }
}
