package me.darkeyedragon.randomtp;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.api.RandomPlugin;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.command.TeleportCommand;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.eco.EcoFactory;
import me.darkeyedragon.randomtp.eco.EcoHandler;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.failsafe.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.listener.PluginLoadListener;
import me.darkeyedragon.randomtp.listener.WorldLoadListener;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.darkeyedragon.randomtp.validator.ValidatorFactory;
import me.darkeyedragon.randomtp.world.location.LocationFactory;
import me.darkeyedragon.randomtp.world.location.search.LocationSearcherFactory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class RandomTeleport extends JavaPlugin implements RandomPlugin {

    private HashMap<UUID, Long> cooldowns;
    private PaperCommandManager manager;
    private Set<PluginLocationValidator> validatorList;
    private WorldQueue worldQueue;
    private ConfigHandler configHandler;
    private LocationFactory locationFactory;
    private DeathTracker deathTracker;
    private BukkitAudiences bukkitAudience;
    //Economy
    private Economy econ;
    private static EcoHandler ecoHandler;

    public static EcoHandler getEcoHandler() {
        return ecoHandler;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        worldQueue.clear();
    }

    public void populateWorldQueue() {
        Bukkit.getLogger().info("Populating WorldQueue");
        long startTime = System.currentTimeMillis();
        for (RandomWorld world : configHandler.getSectionWorld().getWorlds()) {
            //TODO Figure out how to deal with this
            WorldUtil.WORLD_MAP.put(Bukkit.getWorld(world.getUUID()), world);
            //Add a new world to the world queue and generate random locations
            LocationQueue locationQueue = new LocationQueue(configHandler.getSectionQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, this));
            //Subscribe to the locationqueue to be notified of changes
            subscribe(locationQueue, world);
            SectionWorldDetail sectionWorldDetail = getLocationFactory().getWorldConfigSection(world);
            locationQueue.generate(sectionWorldDetail);
            getWorldQueue().put(world, locationQueue);
        }
        Bukkit.getLogger().info("WorldQueue population finished in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (configHandler.getSectionDebug().isShowQueuePopulation()) {
            int size = configHandler.getSectionQueue().getSize();
            locationQueue.subscribe(new QueueListener<RandomLocation>() {
                @Override
                public void onAdd(RandomLocation element) {
                    getLogger().info("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                }

                @Override
                public void onRemove(RandomLocation element) {
                    getLogger().info("Safe location consumed for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                }
            });
        }
    }

    //Economy logic
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    public Set<PluginLocationValidator> getValidatorList() {
        return validatorList;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public WorldQueue getWorldQueue() {
        return worldQueue;
    }

    public LocationQueue getQueue(RandomWorld world) {
        return worldQueue.get(world);
    }

    public PaperCommandManager getManager() {
        return manager;
    }

    public LocationFactory getLocationFactory() {
        return locationFactory;
    }

    public DeathTracker getDeathTracker() {
        return deathTracker;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        bukkitAudience = BukkitAudiences.create(this);
        manager = new PaperCommandManager(this);
        configHandler = new ConfigHandler(this);
        configHandler.reload();
        locationFactory = new LocationFactory(configHandler);
        deathTracker = new DeathTracker(this);
        //check if the first argument is a world or player
        worldQueue = new WorldQueue();
        manager.getCommandContexts().registerContext(PlayerWorldContext.class, c -> {
            String arg1 = c.popFirstArg();
            World world = Bukkit.getWorld(arg1);
            Player player = Bukkit.getPlayer(arg1);
            PlayerWorldContext context = new PlayerWorldContext();
            if (world != null) {
                context.setWorld(world);
                return context;
            } else if (player != null) {
                context.setPlayer(player);
                return context;
            } else {
                throw new InvalidCommandArgument(true);
            }
        });
        cooldowns = new HashMap<>();
        if (setupEconomy()) {
            getLogger().info("Vault found. Hooking into it.");
            EcoFactory.createDefault(econ);
        } else {
            getLogger().warning("Vault not found. Currency based options are disabled.");
        }
        manager.registerCommand(new TeleportCommand(this));
        getServer().getPluginManager().registerEvents(new WorldLoadListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        validatorList = new HashSet<>();
        getLogger().info(ChatColor.AQUA + "======== [Loading validators] ========");
        configHandler.getSectionPlugin().getPlugins().forEach(s -> {
            if (getServer().getPluginManager().getPlugin(s) != null) {
                PluginLocationValidator validator = ValidatorFactory.createFrom(s);
                if (validator != null) {
                    if (validator.isLoaded()) {
                        getLogger().info(ChatColor.GREEN + s + " -- Successfully loaded");
                    } else {
                        getLogger().warning(ChatColor.RED + s + " is not loaded yet. Trying to fix by loading later...");
                    }
                    validatorList.add(validator);
                }
            } else {
                getLogger().warning(ChatColor.RED + s + " -- Not Found.");
            }
        });
        getServer().getPluginManager().registerEvents(new PluginLoadListener(this), this);
        getLogger().info(ChatColor.AQUA + "======================================");
        populateWorldQueue();
    }

    public BukkitAudiences getBukkitAudience() {
        return bukkitAudience;
    }

    public Economy getEcon() {
        return econ;
    }
}
