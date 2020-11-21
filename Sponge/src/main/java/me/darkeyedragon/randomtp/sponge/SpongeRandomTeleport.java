package me.darkeyedragon.randomtp.sponge;

import com.google.inject.Inject;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;
import me.darkeyedragon.randomtp.sponge.config.SpongeConfigHandler;
import me.darkeyedragon.randomtp.sponge.eco.SpongeEcoHandler;
import me.darkeyedragon.randomtp.sponge.logging.SpongeLogger;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;

import java.io.File;
import java.util.Optional;

@Plugin(
        id = "sponge",
        name = "SpongeRandomTeleport",
        version = "1.0-SNAPSHOT"
)
public class SpongeRandomTeleport extends RandomTeleportPluginImpl {

    @Inject
    private Logger logger;
    private PluginLogger pluginLogger;

    @Inject
    private PluginContainer plugin;
    private WorldQueue worldQueue;
    private EcoHandler ecoHandler;
    private LocationFactory locationFactory;
    private  RandomConfigHandler configHandler;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        //super.init();
        configHandler = new SpongeConfigHandler();
        worldQueue = new WorldQueue();
        pluginLogger = new SpongeLogger(logger);
        locationFactory = new LocationFactory(configHandler);
    }

    @Override
    public AddonPlugin getPlugin(String name) {
        //TODO implement
        return null;
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
    public WorldQueue getWorldQueue() {
        return worldQueue;
    }

    @Override
    public LocationFactory getLocationFactory() {
        return locationFactory;
    }

    @Override
    public RandomTeleportPluginImpl getInstance() {
        return this;
    }

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public boolean isPluginLoaded(String name) {
        //TODO implement
        return false;
    }

    @Override
    public AddonManager getAddonManager() {
        //TODO Implement
        return null;
    }
}
