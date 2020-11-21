package me.darkeyedragon.randomtp.common.plugin;

import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;

import java.io.File;

public interface RandomTeleportPlugin<T> {

    AddonPlugin getPlugin(String name);

    PluginLogger getLogger();

    //CommandManager getCommandManager();

    EcoHandler getEcoHandler();

    boolean setupEconomy();

    RandomConfigHandler getConfigHandler();

    WorldQueue getWorldQueue();

    LocationFactory getLocationFactory();
    //DeathTracker getDeathTracker();

    T getInstance();

    File getDataFolder();

    boolean isPluginLoaded(String name);

}
