package me.darkeyedragon.randomtp;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.command.TeleportCommand;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.eco.BukkitEcoHandler;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.failsafe.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.listener.PluginLoadListener;
import me.darkeyedragon.randomtp.listener.WorldLoadListener;
import me.darkeyedragon.randomtp.log.BukkitLogger;
import me.darkeyedragon.randomtp.validator.Validator;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public final class RandomTeleport extends RandomTeleportPluginImpl {


    private final SpigotImpl plugin;

    private HashMap<UUID, Long> cooldowns;
    private PaperCommandManager manager;
    private Set<PluginLocationValidator> validatorList;
    private WorldQueue worldQueue;
    private BukkitConfigHandler bukkitConfigHandler;
    private LocationFactory locationFactory;
    private DeathTracker deathTracker;
    private BukkitAudiences bukkitAudience;
    //Economy
    private Economy econ;
    private static EcoHandler ecoHandler;

    public RandomTeleport(SpigotImpl plugin) {
        this.plugin = plugin;
    }
    PluginLogger logger;


    @Override
    public PluginLogger getLogger() {
        return logger;
    }

    public EcoHandler getEcoHandler() {
        return ecoHandler;
    }

    public void subscribe(LocationQueue locationQueue, RandomWorld world) {
        if (bukkitConfigHandler.getSectionDebug().isShowQueuePopulation()) {
            int size = bukkitConfigHandler.getSectionQueue().getSize();
            locationQueue.subscribe(new QueueListener<RandomLocation>() {
                @Override
                public void onAdd(RandomLocation element) {
                    plugin.getLogger().info("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
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

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    public Set<PluginLocationValidator> getValidatorSet() {
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

    public DeathTracker getDeathTracker() {
        return deathTracker;
    }

    @Override
    public void init() {
        // Plugin startup logic
        logger = new BukkitLogger();
        plugin.saveDefaultConfig();
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
                if(!arg1.isEmpty()){
                    List<Entity> entityList = Bukkit.selectEntities(c.getSender(), arg1);
                    for (Entity entity : entityList) {
                        if(entity instanceof Player){
                            context.addPlayer((Player)entity);
                        }
                    }
                    return context;
                }else{
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
        plugin.getServer().getPluginManager().registerEvents(new WorldLoadListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), plugin);
        validatorList = new HashSet<>();
        plugin.getLogger().info(ChatColor.AQUA + "======== [Loading validators] ========");
        bukkitConfigHandler.getSectionPlugin().getPlugins().forEach(s -> {
            if (plugin.getServer().getPluginManager().getPlugin(s) != null) {
                PluginLocationValidator validator = Validator.getValidator(s);
                if (validator != null) {
                    validator.load();
                    if (validator.isLoaded()) {
                        plugin.getLogger().info(ChatColor.GREEN + s + " -- Successfully loaded");
                    } else {
                        plugin.getLogger().warning(ChatColor.RED + s + " is not loaded yet. Trying to fix by loading later...");
                    }
                    validatorList.add(validator);
                }
            } else {
                plugin.getLogger().warning(ChatColor.RED + s + " -- Not Found.");
            }
        });
        plugin.getServer().getPluginManager().registerEvents(new PluginLoadListener(this), plugin);
        plugin.getLogger().info(ChatColor.AQUA + "======================================");
        super.init();
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
}
