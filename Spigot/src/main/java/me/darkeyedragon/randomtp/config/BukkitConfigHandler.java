package me.darkeyedragon.randomtp.config;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.Dimension;
import me.darkeyedragon.randomtp.api.config.DimensionData;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.*;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.Offset;
import me.darkeyedragon.randomtp.common.config.Blacklist;
import me.darkeyedragon.randomtp.common.util.TimeUtil;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.config.section.*;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.darkeyedragon.randomtp.world.SpigotBiome;
import me.darkeyedragon.randomtp.world.SpigotBlockType;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitConfigHandler implements RandomConfigHandler {

    private final RandomTeleport instance;
    private final JavaPlugin plugin;
    private ConfigMessage configMessage;
    private ConfigQueue configQueue;
    private ConfigWorld configWorld;
    private ConfigTeleport configTeleport;
    private ConfigPlugin configPlugin;
    private ConfigEconomy configEconomy;
    private ConfigDebug configDebug;
    private ConfigBlacklist configBlacklist;

    public BukkitConfigHandler(RandomTeleport instance) {
        this.instance = instance;
        this.plugin = instance.getPlugin();
    }

    /**
     * (re)loads the config.
     * When invalid fiels are found, they will be defaulted to prevent errors.
     */
    public void reload() throws InvalidConfigurationException {
        populateConfigPlugins();
        populateConfigMessage();
        populateConfigQueue();
        populateWorldConfigSection();
        populateConfigTeleport();
        populateConfigDebug();
        populateConfigEconomy();
        populateBlacklist();
    }

    public void populateBlacklist() throws InvalidConfigurationException {
        configBlacklist = new ConfigBlacklist(getBlacklist());
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
        configMessage.getSign().setComponents(getSignLines());
    }

    public void populateConfigQueue() {
        configQueue = new ConfigQueue()
                .size(getQueueSize())
                .initDelay(getInitDelay());
    }

    public void populateWorldConfigSection() {
        configWorld = new ConfigWorld(instance, getOffsets());
    }

    public void populateConfigTeleport() {
        configTeleport = new ConfigTeleport()
                .cooldown(getCooldown())
                .delay(getTeleportDelay())
                .cancelOnMove(isCanceledOnMove())
                .deathTimer(getTeleportDeathTimer())
                .particle(getParticle());
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

    @Override
    public SectionMessage getSectionMessage() {
        return configMessage;
    }

    @Override
    public SectionQueue getSectionQueue() {
        return configQueue;
    }

    @Override
    public SectionWorld getSectionWorld() {
        return configWorld;
    }

    @Override
    public SectionBlacklist getSectionBlacklist() {
        return configBlacklist;
    }

    @Override
    public SectionTeleport getSectionTeleport() {
        return configTeleport;
    }

    @Override
    public SectionPlugin getSectionPlugin() {
        return configPlugin;
    }

    @Override
    public SectionDebug getSectionDebug() {
        return configDebug;
    }

    @Override
    public SectionEconomy getSectionEconomy() {
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

    private Set<SectionWorldDetail> getOffsets() {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds");
        Set<String> keys = Objects.requireNonNull(section).getKeys(false);
        Set<SectionWorldDetail> sectionWorldDetailSet = new HashSet<>(keys.size());
        for (String key : keys) {
            World world = Bukkit.getWorld(key);
            if (world == null) {
                instance.getInstance().getLogger().warn("World " + key + " does not exist! Skipping...");
                continue;
            }
            boolean useWorldBorder = section.getBoolean(key + ".use_worldborder");
            boolean needsWorldPermission = section.getBoolean(key + ".needs_world_permission");
            int radius = section.getInt(key + ".radius");
            int offsetX = section.getInt(key + ".offsetX");
            int offsetZ = section.getInt(key + ".offsetZ");
            plugin.getLogger().info(ChatColor.GREEN + key + " found! Loading...");
            RandomWorld randomWorld = WorldUtil.toRandomWorld(world);
            sectionWorldDetailSet.add(new WorldConfigSection(new Offset(offsetX, offsetZ, radius), randomWorld, useWorldBorder, needsWorldPermission));
        }
        return sectionWorldDetailSet;
    }

    private String getInsufficientFundsMessage() {
        return plugin.getConfig().getString("message.economy.insufficient_funds", "&cYou do not have enough money to rtp!");
    }

    private double getPrice() {
        return plugin.getConfig().getDouble("economy.price", 0);
    }

    private String getPaymentMessage() {
        String message = plugin.getConfig().getString("message.economy.payment", "&aYou just paid &b%price &ato rtp!");
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replaceAll("%price", getPrice() + "");
        return message;
    }

    private long getCooldown() throws NumberFormatException {
        String message = plugin.getConfig().getString("teleport.cooldown", "60m");
        return TimeUtil.stringToLong(message);
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

    private long getTeleportDeathTimer() {
        String message = plugin.getConfig().getString("teleport.death_timer", "10s");
        if (message != null) {
            return TimeUtil.stringToTicks(message);
        }
        throw new NumberFormatException("Not a valid number");
    }

    public List<String> getSignLines() {
        return plugin.getConfig().getStringList("message.sign");
    }

    public Blacklist getBlacklist() throws InvalidConfigurationException {
        Blacklist blacklist = new Blacklist();
        for (Dimension dimension : Dimension.values()) {
            blacklist.addDimensionData(dimension, getDimData(dimension));
        }
        return blacklist;
    }

    private DimensionData getDimData(Dimension dimension) throws InvalidConfigurationException {
        ConfigurationSection blacklistSec = plugin.getConfig().getConfigurationSection("blacklist");
        if (blacklistSec == null) throw new InvalidConfigurationException("blacklist section missing!");
        DimensionData dimensionData = new DimensionData();

        ConfigurationSection section;
        switch (dimension) {
            case GLOBAL:
                section = blacklistSec.getConfigurationSection("global");
                break;
            case OVERWORLD:
                section = blacklistSec.getConfigurationSection("overworld");
                break;
            case NETHER:
                section = blacklistSec.getConfigurationSection("nether");
                break;
            case END:
                section = blacklistSec.getConfigurationSection("end");
                break;
            default:
                section = null;
                break;
        }
        if (section == null) throw new InvalidConfigurationException("blacklist.global section missing!");
        List<String> blockStrings = section.getStringList("block");
        Material[] materials = Material.values();
        for (String s : blockStrings) {
            if (s.startsWith("$")) {
                Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
                for (Tag<Material> tag : tags) {
                    if (tag.getKey().getKey().equalsIgnoreCase(s.substring(1))) {
                        for (Material value : tag.getValues()) {
                            dimensionData.addBlockType(new SpigotBlockType(value));
                        }
                    }
                }
            } else {
                Pattern pattern = Pattern.compile(s);
                for (Material material : materials) {
                    Matcher matcher = pattern.matcher(material.name());
                    while (matcher.find()) {
                        try {
                            dimensionData.addBlockType(new SpigotBlockType(Material.valueOf(matcher.group(0))));
                        } catch (IllegalArgumentException ex) {
                            instance.getInstance().getLogger().warn(s + " is not a valid block.");
                        }
                    }
                }
            }
        }
        if (dimension == Dimension.GLOBAL) {
            return dimensionData;
        }
        List<String> biomeStrings = section.getStringList("biome");
        for (String s : biomeStrings) {
            Pattern pattern = Pattern.compile(s);
            for (Material material : materials) {
                Matcher matcher = pattern.matcher(material.name());
                while (matcher.find()) {
                    try {
                        dimensionData.addBiome(new SpigotBiome(Biome.valueOf(s.toUpperCase())));
                    } catch (IllegalArgumentException ex) {
                        instance.getInstance().getLogger().warn(s + " is not a valid biome.");
                    }
                }
            }
        }
        return dimensionData;
    }

    private TeleportParticle<Particle> getParticle() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("teleport");
        String particleString = section.getString("particle");
        if(particleString == null || particleString.equalsIgnoreCase("none")) {
            return new TeleportParticle<>(null, 0);
        }
        String[] split = particleString.split(":");
        try{
            Particle particle = Particle.valueOf(split[0].toUpperCase());
            int amount = Integer.parseInt(split[1]);
            return new TeleportParticle<>(particle, amount);
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
        }
        return new TeleportParticle<>(null, 0);
    }

    public ConfigBlacklist getConfigBlacklist() {
        return configBlacklist;
    }
}
