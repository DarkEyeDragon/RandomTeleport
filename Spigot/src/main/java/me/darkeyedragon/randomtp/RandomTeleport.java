package me.darkeyedragon.randomtp;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import me.darkeyedragon.randomtp.addon.SpigotAddonPlugin;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.logging.PluginLogger;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.metric.Metric;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.TeleportHandler;
import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.command.RandomTeleportCommand;
import me.darkeyedragon.randomtp.common.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.eco.BukkitEcoHandler;
import me.darkeyedragon.randomtp.failsafe.SpigotDeathTracker;
import me.darkeyedragon.randomtp.failsafe.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.listener.ServerLoadListener;
import me.darkeyedragon.randomtp.listener.WorldLoadListener;
import me.darkeyedragon.randomtp.log.BukkitLogger;
import me.darkeyedragon.randomtp.teleport.SpigotCooldownHandler;
import me.darkeyedragon.randomtp.world.SpigotPlayerHandler;
import me.darkeyedragon.randomtp.world.SpigotWorldHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public final class RandomTeleport extends RandomTeleportPluginImpl {

    private final SpigotImpl plugin;

    private PaperCommandManager manager;
    private Set<RandomLocationValidator> validatorList;
    private WorldQueue worldQueue;
    private BukkitConfigHandler bukkitConfigHandler;
    private DeathTracker deathTracker;
    private BukkitAudiences bukkitAudience;
    private RandomWorldHandler worldHandler;
    private TeleportHandler teleportHandler;
    private PlayerHandler playerHandler;
    private Metric metric;
    private CooldownHandler cooldownHandler;

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
        playerHandler = new SpigotPlayerHandler();
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
        //locationFactory = new LocationFactory(bukkitConfigHandler);
        worldHandler = new SpigotWorldHandler(this);
        deathTracker = new SpigotDeathTracker(this);
        //check if the first argument is a world or player
        worldQueue = new WorldQueue();
        manager.getCommandCompletions().registerAsyncCompletion("addonFiles", context -> ImmutableList.copyOf(addonManager.getFileNames()));
        manager.getCommandCompletions().registerAsyncCompletion("addonNames", context -> ImmutableList.copyOf(addonManager.getAddons().keySet()));
        manager.getCommandCompletions().registerAsyncCompletion("particles", context -> Arrays.stream(Particle.values()).map(Particle::name).collect(Collectors.toList()));
        manager.getCommandContexts().registerContext(PlayerWorldContext.class, c -> {
            String arg1 = c.popFirstArg();
            RandomWorld randomWorld = worldHandler.getWorld(arg1);
            RandomPlayer player = playerHandler.getPlayer(arg1);
            PlayerWorldContext context = new PlayerWorldContext();
            if (randomWorld != null) {
                context.setWorld(randomWorld);
                return context;
            } else if (player != null) {
                context.addPlayer(player);
                return context;
            } else {
                if (!arg1.isEmpty()) {
                    List<Entity> entityList = Bukkit.selectEntities(c.getSender(), arg1);
                    for (Entity entity : entityList) {
                        if (entity instanceof Player) {
                            context.addPlayer(playerHandler.getPlayer(entity.getUniqueId()));
                        }
                    }
                    return context;
                } else {
                    throw new InvalidCommandArgument(true);
                }
            }
        });
        if (setupEconomy()) {
            plugin.getLogger().info("Vault found. Hooking into it.");
        } else {
            plugin.getLogger().warning("Vault not found. Currency based options are disabled.");
        }
        manager.registerCommand(new RandomTeleportCommand(this));
        manager.getCommandCompletions().registerAsyncCompletion("filteredWorlds", context -> {
            Set<RandomWorld> randomWorlds = bukkitConfigHandler.getSectionWorld().getWorlds();
            Iterator<RandomWorld> randomWorldIterator = randomWorlds.iterator();
            while (randomWorldIterator.hasNext()) {
                RandomWorld randomWorld = randomWorldIterator.next();
                SectionWorldDetail sectionWorldDetail = bukkitConfigHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
                if (sectionWorldDetail.needsWorldPermission() && !(context.getSender() instanceof ConsoleCommandSender)) {
                    if (!context.getPlayer().hasPermission("rtp.world." + randomWorld.getName())) {
                        randomWorldIterator.remove();
                    }
                }
            }
            return randomWorlds.stream().map(RandomWorld::getName).collect(Collectors.toList());
        });
        plugin.getServer().getPluginManager().registerEvents(new WorldLoadListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), plugin);
        validatorList = new HashSet<>();
        pluginManager = Bukkit.getPluginManager();
        cooldownHandler = new SpigotCooldownHandler();
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

    public Set<RandomLocationValidator> getValidatorSet() {
        return validatorList;
    }

    public BukkitConfigHandler getConfigHandler() {
        return bukkitConfigHandler;
    }

    public LocationQueue getQueue(RandomWorld world) {
        return worldQueue.get(world);
    }

    public PaperCommandManager getCommandManager() {
        return manager;
    }

    //TODO REPLACE
    /*public LocationFactory getLocationFactory() {
        return locationFactory;
    }*/

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

    @Override
    public BukkitAudiences getAudience() {
        return bukkitAudience;
    }

    @Override
    public MessageHandler getMessageHandler() {
        throw new RuntimeException("Not implemented");
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

    public RandomWorldHandler getWorldHandler() {
        return worldHandler;
    }

}
