package me.darkeyedragon.randomtp;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import me.darkeyedragon.randomtp.addon.SpigotAddonPlugin;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.command.TeleportCommand;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.eco.BukkitEcoHandler;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.failsafe.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.listener.ServerLoadListener;
import me.darkeyedragon.randomtp.listener.WorldLoadListener;
import me.darkeyedragon.randomtp.log.BukkitLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.*;

public final class RandomTeleport extends RandomTeleportPluginImpl {

    private final SpigotImpl plugin;

    private HashMap<UUID, Long> cooldowns;
    private PaperCommandManager manager;
    private Set<RandomLocationValidator> validatorList;
    private WorldQueue worldQueue;
    private BukkitConfigHandler bukkitConfigHandler;
    private LocationFactory locationFactory;
    private DeathTracker deathTracker;
    private BukkitAudiences bukkitAudience;

    //Economy
    private Economy econ;
    private static EcoHandler ecoHandler;

    PluginLogger logger;
    private AddonManager addonManager;

    private PluginManager pluginManager;

    public RandomTeleport(SpigotImpl plugin) {
        this.plugin = plugin;
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (bukkitConfigHandler.getSectionDebug().isShowQueuePopulation()) {
            int size = bukkitConfigHandler.getSectionQueue().getSize();
            locationQueue.subscribe(new QueueListener<RandomLocation>() {
                @Override
                public void onAdd(RandomLocation element) {
                    plugin.getLogger().info("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + size + ") in " + element.getTries() + " tries");
                }

                @Override
                public void onRemove(RandomLocation element) {
                    plugin.getLogger().info("Safe location consumed for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                }
            });
        }
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
        econ = rsp.getProvider();
        ecoHandler = new BukkitEcoHandler(econ);
        return true;
    }

    public void init() {
        // Plugin startup logic
        loadListeners();
        logger = new BukkitLogger(this);
        addonManager = new AddonManager(this, logger);
        if(addonManager.createAddonDir()){
            logger.info("No addon folder. Creating one...");
        }
        bukkitAudience = BukkitAudiences.create(plugin);
        manager = new PaperCommandManager(plugin);
        bukkitConfigHandler = new BukkitConfigHandler(this);
        try {
            bukkitConfigHandler.reload();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        locationFactory = new LocationFactory(bukkitConfigHandler);
        deathTracker = new DeathTracker(this);
        //check if the first argument is a world or player
        worldQueue = new WorldQueue();
        manager.getCommandCompletions().registerAsyncCompletion("addonFiles", context -> ImmutableList.copyOf(addonManager.getFileNames()));
        manager.getCommandCompletions().registerAsyncCompletion("addonNames", context -> ImmutableList.copyOf(addonManager.getAddons().keySet()));
        manager.getCommandContexts().registerContext(PlayerWorldContext.class, c -> {
            String arg1 = c.popFirstArg();
            World world = Bukkit.getWorld(arg1);
            Player player = Bukkit.getPlayer(arg1);
            PlayerWorldContext context = new PlayerWorldContext();
            if (world != null) {
                context.setWorld(world);
                return context;
            } else if (player != null) {
                context.addPlayer(player);
                return context;
            } else {
                if (!arg1.isEmpty()) {
                    List<Entity> entityList = Bukkit.selectEntities(c.getSender(), arg1);
                    for (Entity entity : entityList) {
                        if (entity instanceof Player) {
                            context.addPlayer((Player) entity);
                        }
                    }
                    return context;
                } else {
                    throw new InvalidCommandArgument(true);
                }
            }
        });
        cooldowns = new HashMap<>();
        if (setupEconomy()) {
            plugin.getLogger().info("Vault found. Hooking into it.");
        } else {
            plugin.getLogger().warning("Vault not found. Currency based options are disabled.");
        }
        manager.registerCommand(new TeleportCommand((SpigotImpl) plugin));
        validatorList = new HashSet<>();
        pluginManager = Bukkit.getPluginManager();
    }

    private void loadListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new WorldLoadListener(this), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(this), plugin);
        pluginManager.registerEvents(new ServerLoadListener(this), plugin);
    }

    @Override
    public AddonManager getAddonManager() {
        return addonManager;
    }

    public SpigotImpl getPlugin() {
        return plugin;
    }

    public BukkitAudiences getBukkitAudience() {
        return bukkitAudience;
    }

    public Economy getEcon() {
        return econ;
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

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    public Set<RandomLocationValidator> getValidatorSet() {
        return validatorList;
    }

    public BukkitConfigHandler getConfigHandler() {
        return bukkitConfigHandler;
    }

    public WorldQueue getWorldQueue() {
        return worldQueue;
    }

    public LocationQueue getQueue(RandomWorld world) {
        return worldQueue.get(world);
    }

    public PaperCommandManager getCommandManager() {
        return manager;
    }

    public LocationFactory getLocationFactory() {
        return locationFactory;
    }

    @Override
    public RandomTeleportPluginImpl getInstance() {
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

    public DeathTracker getDeathTracker() {
        return deathTracker;
    }

    public PaperCommandManager getManager() {
        return manager;
    }

    public Set<RandomLocationValidator> getValidatorList() {
        return validatorList;
    }

    public BukkitConfigHandler getBukkitConfigHandler() {
        return bukkitConfigHandler;
    }
}
