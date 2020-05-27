package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import me.darkeyedragon.randomtp.util.CustomTime;
import me.darkeyedragon.randomtp.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final RandomTeleport plugin;
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

    public String getNoWorldPermissionMessage(World world) {
        String message = plugin.getConfig().getString("message.no_world_permission");
        if (message != null)
            message = message.replace("%world", world.getName());
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getTeleportMessage() {
        String message = plugin.getConfig().getString("message.teleport");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    public String getBlacklistMessage() {
        String message = plugin.getConfig().getString("message.blacklisted");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    public String getDepletedQueueMessage() {
        String message = plugin.getConfig().getString("message.depleted_queue", "&6Locations queue depleted... Forcing generation of a new location");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
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

    public Map<World, WorldConfigSection> getOffsets() {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds");
        Set<String> keys = Objects.requireNonNull(section).getKeys(false);
        Map<World, WorldConfigSection> offsetMap = new HashMap<>(keys.size());
        for (String key : keys) {
            boolean useWorldBorder = section.getBoolean(key + ".use_worldborder");
            boolean needsWorldPermission = section.getBoolean(key + ".needs_world_permission");
            int radius = section.getInt(key + ".radius");
            int offsetX = section.getInt(key + ".offsetX");
            int offsetZ = section.getInt(key + ".offsetZ");
            World world = Bukkit.getWorld(key);
            offsetMap.put(world, new WorldConfigSection(offsetX, offsetZ, radius, world, useWorldBorder, needsWorldPermission));
        }
        return offsetMap;
    }

    public Set<World> getWorlds() {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds");
        Set<String> keys = Objects.requireNonNull(section).getKeys(false);
        return keys.stream().map(Bukkit::getWorld).collect(Collectors.toSet());
    }

    public String getInsufficientFundsMessage() {
        return plugin.getConfig().getString("message.economy.insufficient_funds");
    }

    public double getPrice() {
        return plugin.getConfig().getDouble("economy.price", 0);
    }

    public String getPaymentMessage() {
        String message = plugin.getConfig().getString("message.economy.payment", "&aYou just paid &b%price &ato rtp!");
        if(message == null){
            return "";
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replaceAll("%price", getPrice()+"");
        return message;
    }

    public boolean useEco() {
        return getPrice() > 0;
    }

    public boolean addWorld(WorldConfigSection worldConfigSection) {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds");
        if (section == null) {
            return false;
        }
        section.set(worldConfigSection.getWorld().getName() + ".use_worldborder", worldConfigSection.useWorldBorder());
        section.set(worldConfigSection.getWorld().getName() + ".radius", worldConfigSection.getRadius());
        section.set(worldConfigSection.getWorld().getName() + ".offsetX", worldConfigSection.getX());
        section.set(worldConfigSection.getWorld().getName() + ".offsetZ", worldConfigSection.getZ());
        plugin.saveConfig();
        return true;
    }

    private long formatCooldown() throws NumberFormatException {
        String message = plugin.getConfig().getString("teleport.cooldown");
        if (message != null) {
            return TimeUtil.stringToLong(message);
        }
        throw new NumberFormatException("Not a valid format");
    }

    public long getTeleportDelay() {
        String message = plugin.getConfig().getString("teleport.delay", "0s");
        if (message != null) {
            return TimeUtil.stringToTicks(message);
        }
        throw new NumberFormatException("Not a valid number");
    }

    public List<World> getWorldsBlacklist() {
        List<String> strings = plugin.getConfig().getStringList("blacklist.worlds");
        return strings.stream().map(Bukkit::getWorld).collect(Collectors.toList());
    }

    public int getInitDelay() {
        return plugin.getConfig().getInt("queue.init_delay", 60);
    }

    public boolean getDebugShowQueuePopulation() {
        return plugin.getConfig().getBoolean("debug.show_queue_population", true);
    }

    public long getCooldown() {
        if (cooldown == -1) {
            cooldown = formatCooldown();
        }
        return cooldown;
    }

    public List<String> getPlugins() {
        return plugin.getConfig().getStringList("plugins");
    }

    public boolean isWhitelist() {
        return plugin.getConfig().getBoolean("blacklist.isWhitelist");
    }

    public int getQueueSize() {
        return plugin.getConfig().getInt("queue.size", 5);
    }

    public String getCancelMessage() {
        String message = plugin.getConfig().getString("message.teleport_canceled", "&cYou moved! Teleportation canceled");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    public String getInitTeleportDelay() {
        String message = plugin.getConfig().getString("message.initteleport_delay", "&aYou will be teleported in &6%s seconds. Do not move");
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime time = TimeUtil.formatTime(getTeleportDelay() * 50);
            message = TimeUtil.toFormattedString(message, time);
        }
        return message;
    }

    public boolean isCanceledOnMove() {
        return plugin.getConfig().getBoolean("teleport.cancel_on_move", false);
    }
}
