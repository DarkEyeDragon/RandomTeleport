package me.darkeyedragon.randomtp.sponge;

import co.aikar.commands.SpongeCommandManager;
import com.google.inject.Inject;
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
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomMaterialHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.command.DebugCommand;
import me.darkeyedragon.randomtp.common.command.RandomTeleportCommand;
import me.darkeyedragon.randomtp.common.config.serializer.ConfigTypeSerializerCollection;
import me.darkeyedragon.randomtp.common.failsafe.CommonDeathTracker;
import me.darkeyedragon.randomtp.common.message.CommonMessageHandler;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.common.stat.BStats;
import me.darkeyedragon.randomtp.common.stat.NoConsentException;
import me.darkeyedragon.randomtp.common.teleport.CommonCooldownHandler;
import me.darkeyedragon.randomtp.common.world.WorldHandler;
import me.darkeyedragon.randomtp.sponge.addon.SpongeAddonPlugin;
import me.darkeyedragon.randomtp.sponge.command.completion.Registrar;
import me.darkeyedragon.randomtp.sponge.config.SpongeConfigHandler;
import me.darkeyedragon.randomtp.sponge.eco.SpongeEcoHandler;
import me.darkeyedragon.randomtp.sponge.listener.PlayerDeathListener;
import me.darkeyedragon.randomtp.sponge.logging.SpongeLogger;
import me.darkeyedragon.randomtp.sponge.scheduler.SpongeScheduler;
import me.darkeyedragon.randomtp.sponge.world.SpongeBiomeHandler;
import me.darkeyedragon.randomtp.sponge.world.SpongeMaterialHandler;
import me.darkeyedragon.randomtp.sponge.world.SpongePlayerHandler;
import me.darkeyedragon.randomtp.sponge.world.SpongeWorldHandler;
import me.darkeyedragon.randomtp.sponge.world.search.SpongeEndLocationSearcher;
import me.darkeyedragon.randomtp.sponge.world.search.SpongeNetherLocationSearcher;
import me.darkeyedragon.randomtp.sponge.world.search.SpongeOverworldLocationSearcher;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.util.metric.MetricsConfigManager;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "randomteleport",
        name = "RandomTeleport",
        version = "1.0.0"
)
public class SpongeRandomTeleport extends RandomTeleportPluginImpl {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    private PluginContainer plugin;

    @Inject
    private Game game;

    @Inject
    private MetricsConfigManager metricsConfigManager;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    private PluginLogger pluginLogger;
    private EcoHandler ecoHandler;
    private SpongeConfigHandler configHandler;
    private DeathTracker deathTracker;
    private AudienceProvider audience;
    private MessageHandler messageHandler;
    private Metric metric;
    private CooldownHandler cooldownHandler;
    private AddonManager addonManager;
    private RandomMaterialHandler materialHandler;
    private RandomWorldHandler worldHandler;
    private PlayerHandler playerHandler;
    private SpongeCommandManager commandManager;
    private Scheduler scheduler;

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        audience = SpongeAudiences.create(this.plugin, game);
        pluginLogger = new SpongeLogger(audience, logger);
        scheduler = new SpongeScheduler(plugin, Sponge.getScheduler());
        HoconConfigurationLoader configLoader = HoconConfigurationLoader
                .builder()
                .path(defaultConfig)
                .defaultOptions(
                        configurationOptions -> configurationOptions.serializers(new ConfigTypeSerializerCollection(this).build())
                )
                .build();
        materialHandler = new SpongeMaterialHandler();
        worldHandler = new SpongeWorldHandler(this, new SpongeBiomeHandler());
        configHandler = new SpongeConfigHandler(this, configLoader);
        try {
            configHandler.saveDefaultConfig();
            configHandler.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addonManager = new AddonManager(this, this.getLogger());
        if (addonManager.createAddonDir()) {
            logger.info("No addon folder. Creating one...");
        }
        if (hasConsent()) {
            metric = new BStats();
        } else {
            logger.warn("Please run \"/sponge metrics randomteleport enable\" to allow stat collection! It helps me keep track of important metrics.");
        }
        cooldownHandler = new CommonCooldownHandler();
        messageHandler = new CommonMessageHandler(this);
        playerHandler = new SpongePlayerHandler();
        deathTracker = new CommonDeathTracker(this);
        commandManager = new SpongeCommandManager(plugin);
        commandManager.enableUnstableAPI("help");
        //commandManager.enableUnstableAPI("brigadier");
        //Register all completions and contexts for ACF
        Registrar.registerCompletions(commandManager, addonManager, configHandler);
        Registrar.registerContexts(commandManager, worldHandler, playerHandler);
        commandManager.registerCommand(new RandomTeleportCommand(this));
        commandManager.registerCommand(new DebugCommand(this));
        registerEvents();
    }

    @Listener
    public void onGameLoadComplete(GameStartedServerEvent event) {
        WorldHandler.registerLocationSearcher(RandomEnvironment.OVERWORLD, new SpongeOverworldLocationSearcher(this));
        WorldHandler.registerLocationSearcher(RandomEnvironment.NETHER, new SpongeNetherLocationSearcher(this));
        WorldHandler.registerLocationSearcher(RandomEnvironment.THE_END, new SpongeEndLocationSearcher(this));
        getLogger().info(Component.text("======== [Loading validators] ========").color(TextColor.color(Color.CYAN.getRgb())));
        try {
            getAddonManager().instantiateAllLocal();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        getLogger().info(Component.text("======================================").color(TextColor.color(Color.CYAN.getRgb())));
        getWorldHandler().populateWorldQueue();
    }

    private void registerEvents() {
        Sponge.getEventManager().registerListeners(this, new PlayerDeathListener(this));
    }

    @Override
    public AddonPlugin getPlugin(String name) {
        return SpongeAddonPlugin.create(name);
    }

    @Override
    public PluginLogger getLogger() {
        return pluginLogger;
    }

    @Override
    public EcoHandler getEcoHandler() {
        return ecoHandler;
    }


    @Override
    public boolean setupEconomy() {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            return false;
        }
        EconomyService economyService = serviceOpt.get();
        ecoHandler = new SpongeEcoHandler(plugin, economyService);
        return true;
    }

    @Override
    public RandomConfigHandler getConfigHandler() {
        return configHandler;
    }

    @Override
    public RandomWorldHandler getWorldHandler() {
        return worldHandler;
    }

    @Override
    public DeathTracker getDeathTracker() {
        return deathTracker;
    }

    @Override
    public RandomTeleportPluginImpl getInstance() {
        return this;
    }

    @Override
    public File getDataFolder() {
        return defaultConfig.getParent().toFile();
    }

    @Override
    public Path getConfigPath() {
        return defaultConfig;
    }

    @Override
    public boolean isPluginLoaded(String name) {
        //TODO implement
        return false;
    }

    @Override
    public AudienceProvider getAudience() {
        return audience;
    }

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public void reloadConfig() {
        configHandler.reload();
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public Metric getStats() throws NoConsentException {
        if (metric == null) {
            throw new NoConsentException("User did not give consent. No stats may be recorded.");
        }
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
    public AddonManager getAddonManager() {
        return addonManager;
    }

    @Override
    public RandomMaterialHandler getMaterialHandler() {
        return materialHandler;
    }

    @Override
    public boolean hasConsent() {
        return this.metricsConfigManager.getCollectionState(this.plugin).asBoolean();
    }
}
