package me.darkeyedragon.randomtp;

import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.addon.SpigotAddonPlugin;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.logging.PluginLogger;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.metric.Metric;
import me.darkeyedragon.randomtp.api.scheduler.Scheduler;
import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.RandomMaterialHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.command.completion.Registrar;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.command.RandomTeleportCommand;
import me.darkeyedragon.randomtp.common.config.CommonConfigHandler;
import me.darkeyedragon.randomtp.common.message.CommonMessageHandler;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.eco.BukkitEcoHandler;
import me.darkeyedragon.randomtp.failsafe.SpigotDeathTracker;
import me.darkeyedragon.randomtp.failsafe.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.listener.ServerLoadListener;
import me.darkeyedragon.randomtp.listener.WorldBorderChangeListener;
import me.darkeyedragon.randomtp.listener.WorldListener;
import me.darkeyedragon.randomtp.log.BukkitLogger;
import me.darkeyedragon.randomtp.scheduler.SpigotScheduler;
import me.darkeyedragon.randomtp.teleport.SpigotCooldownHandler;
import me.darkeyedragon.randomtp.world.SpigotBiomeHandler;
import me.darkeyedragon.randomtp.world.SpigotMaterialHandler;
import me.darkeyedragon.randomtp.world.SpigotPlayerHandler;
import me.darkeyedragon.randomtp.world.SpigotWorldHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public final class RandomTeleport extends RandomTeleportPluginImpl {

    private final SpigotImpl plugin;

    private PaperCommandManager commandManager;
    private RandomConfigHandler configHandler;
    private DeathTracker deathTracker;
    private BukkitAudiences bukkitAudience;
    private RandomWorldHandler worldHandler;
    private RandomMaterialHandler materialHandler;
    private PlayerHandler playerHandler;
    private Metric metric;
    private CooldownHandler cooldownHandler;
    private static Scheduler scheduler;

    private static EcoHandler ecoHandler;

    PluginLogger logger;
    private AddonManager addonManager;

    private PluginManager pluginManager;
    private MessageHandler messageHandler;

    public RandomTeleport(SpigotImpl plugin) {
        this.plugin = plugin;
    }


    //Economy logic
    @Override
    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        //Economy
        Economy econ = rsp.getProvider();
        ecoHandler = new BukkitEcoHandler(econ);
        return true;
    }

    public void init() {
        // Plugin startup logic
        materialHandler = new SpigotMaterialHandler();
        worldHandler = new SpigotWorldHandler(this, new SpigotBiomeHandler());
        configHandler = new CommonConfigHandler(this);
        configHandler.reload();
        scheduler = new SpigotScheduler(this);
        playerHandler = new SpigotPlayerHandler();
        messageHandler = new CommonMessageHandler(this);
        logger = new BukkitLogger(this);
        addonManager = new AddonManager(this, logger);
        if (addonManager.createAddonDir()) {
            logger.info("No addon folder. Creating one...");
        }
        bukkitAudience = BukkitAudiences.create(plugin);
        commandManager = new PaperCommandManager(plugin);
        deathTracker = new SpigotDeathTracker(this);
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
        //Register all completions and contexts for ACF
        Registrar.registerCompletions(commandManager, addonManager, configHandler);
        Registrar.registerContexts(commandManager, worldHandler, playerHandler);
        commandManager.registerCommand(new RandomTeleportCommand(this));

        if (setupEconomy()) {
            plugin.getLogger().info("Vault found. Hooking into it.");
        } else {
            plugin.getLogger().warning("Vault not found. Currency based options are disabled.");
        }
        pluginManager = Bukkit.getPluginManager();
        cooldownHandler = new SpigotCooldownHandler();

        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new WorldListener(this), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(this), plugin);
        pluginManager.registerEvents(new ServerLoadListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new WorldBorderChangeListener(this), plugin);
    }

    @Override
    public AddonManager getAddonManager() {
        return addonManager;
    }

    @Override
    public RandomMaterialHandler getMaterialHandler() {
        return materialHandler;
    }

    public SpigotImpl getPlugin() {
        return plugin;
    }

    public BukkitAudiences getBukkitAudience() {
        return bukkitAudience;
    }

    @Override
    public AddonPlugin getPlugin(String name) {
        return SpigotAddonPlugin.create(name);
    }

    @Override
    public PluginLogger getLogger() {
        return logger;
    }

    public EcoHandler getEcoHandler() {
        return ecoHandler;
    }

    public RandomConfigHandler getConfigHandler() {
        return configHandler;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public RandomTeleport getInstance() {
        return this;
    }

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public boolean isPluginLoaded(String name) {
        return pluginManager.isPluginEnabled(name);
    }

    @Override
    public BukkitAudiences getAudience() {
        return bukkitAudience;
    }

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public void reloadConfig() {
        getConfigHandler().reload();
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public Metric getStats() {
        return metric;
    }

    @Override
    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public DeathTracker getDeathTracker() {
        return deathTracker;
    }

    public RandomWorldHandler getWorldHandler() {
        return worldHandler;
    }

}
