package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.data.*;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import me.darkeyedragon.randomtp.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ConfigHandler {

    private final RandomTeleport plugin;
    private ConfigMessage configMessage;
    private ConfigQueue configQueue;
    private ConfigWorld configWorld;
    private ConfigTeleport configTeleport;
    private ConfigPlugin configPlugin;
    private ConfigEconomy configEconomy;
    private ConfigDebug configDebug;

    private long cooldown = -1;

    public ConfigHandler(RandomTeleport plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        populateConfigMessage();
        populateConfigQueue();
        populateWorldConfigSection();
        populateConfigTeleport();
        populateConfigPlugins();
        populateConfigDebug();
        populateConfigEconomy();
    }

    public void populateConfigMessage() {
        configMessage = new ConfigMessage()
                .init(getInitTeleportMessage())
                .initTeleportDelay(getInitTeleportDelay())
                .teleportCanceled(getCancelMessage())
                .teleport(getTeleportMessage())
                .depletedQueue(getDepletedQueueMessage())
                .countdown(getCountdownRemainingMessage())
                .noWorldPermission(getNoWorldPermissionMessage())
                .emptyQueue(getEmptyQueueMessage());
        configMessage.getEconomy()
                .insufficientFunds(getInsufficientFundsMessage())
                .payment(getPaymentMessage());
    }

    public void populateConfigQueue() {
        configQueue = new ConfigQueue()
                .size(getQueueSize())
                .initDelay(getInitDelay());
    }

    public void populateWorldConfigSection() {
        configWorld = new ConfigWorld(plugin).set(getOffsets());
    }

    public void populateConfigTeleport() {
        configTeleport = new ConfigTeleport()
                .cooldown(getCooldown())
                .delay(getTeleportDelay())
                .cancelOnMove(isCanceledOnMove());
    }

    public void populateConfigDebug() {
        configDebug = new ConfigDebug()
                .showQueuePopulation(getDebugShowQueuePopulation());
    }

    public void populateConfigPlugins() {
        configPlugin = new ConfigPlugin()
                .addAll(getPlugins());
    }


    public void populateConfigEconomy() {
        configEconomy = new ConfigEconomy()
                .price(getPrice());
    }

    public RandomTeleport getPlugin() {
        return plugin;
    }

    public ConfigMessage getConfigMessage() {
        return configMessage;
    }

    public ConfigQueue getConfigQueue() {
        return configQueue;
    }

    public ConfigWorld getConfigWorld() {
        return configWorld;
    }

    public ConfigTeleport getConfigTeleport() {
        return configTeleport;
    }

    public ConfigPlugin getConfigPlugin() {
        return configPlugin;
    }

    public ConfigDebug getConfigDebug() {
        return configDebug;
    }

    public ConfigEconomy getConfigEconomy() {
        return configEconomy;
    }

    private String getInitTeleportMessage() {
        return plugin.getConfig().getString("message.initteleport");
    }

    private String getNoWorldPermissionMessage() {
        return plugin.getConfig().getString("message.no_world_permission");
    }

    private String getTeleportMessage() {
        return plugin.getConfig().getString("message.teleport");
    }

    private String getDepletedQueueMessage() {
        return plugin.getConfig().getString("message.depleted_queue", "&6Locations queue depleted... Forcing generation of a new location");
    }

    private String getCountdownRemainingMessage() {
        return plugin.getConfig().getString("message.countdown");
    }

    private Map<World, WorldConfigSection> getOffsets() {
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

    private String getInsufficientFundsMessage() {
        return plugin.getConfig().getString("message.economy.insufficient_funds");
    }

    private double getPrice() {
        return plugin.getConfig().getDouble("economy.price", 0);
    }

    private String getPaymentMessage() {
        String message = plugin.getConfig().getString("message.economy.payment", "&aYou just paid &b%price &ato rtp!");
        if (message == null) {
            return "";
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replaceAll("%price", getPrice() + "");
        return message;
    }

    private long formatCooldown() throws NumberFormatException {
        String message = plugin.getConfig().getString("teleport.cooldown");
        if (message != null) {
            return TimeUtil.stringToLong(message);
        }
        throw new NumberFormatException("Not a valid format");
    }

    private long getTeleportDelay() {
        String message = plugin.getConfig().getString("teleport.delay", "0s");
        if (message != null) {
            return TimeUtil.stringToTicks(message);
        }
        throw new NumberFormatException("Not a valid number");
    }

    private int getInitDelay() {
        return plugin.getConfig().getInt("queue.init_delay", 60);
    }

    private boolean getDebugShowQueuePopulation() {
        return plugin.getConfig().getBoolean("debug.show_queue_population", true);
    }

    private long getCooldown() {
        if (cooldown == -1) {
            cooldown = formatCooldown();
        }
        return cooldown;
    }

    private List<String> getPlugins() {
        return plugin.getConfig().getStringList("plugins");
    }

    private int getQueueSize() {
        return plugin.getConfig().getInt("queue.size", 5);
    }

    private String getCancelMessage() {
        return plugin.getConfig().getString("message.teleport_canceled", "&cYou moved! Teleportation canceled");
    }

    private String getInitTeleportDelay() {
        return plugin.getConfig().getString("message.initteleport_delay", "&aYou will be teleported in &6%s seconds. Do not move");
    }

    private boolean isCanceledOnMove() {
        return plugin.getConfig().getBoolean("teleport.cancel_on_move", false);
    }

    private String getEmptyQueueMessage() {
        return plugin.getConfig().getString("message.empty_queue", "&cThere are no locations available for this world! Try again in a bit or ask an admin to reload the config.");
    }


    public void setTeleportPrice(double price) {
        plugin.getConfig().set("economy.price", price);
        plugin.saveConfig();
    }
}
