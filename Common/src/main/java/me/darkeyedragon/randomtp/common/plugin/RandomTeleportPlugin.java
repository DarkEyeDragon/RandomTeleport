package me.darkeyedragon.randomtp.common.plugin;

import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;

import java.io.File;
import java.util.Set;

public interface RandomTeleportPlugin<T> {


    PluginLogger getLogger();

    //CommandManager getCommandManager();

    EcoHandler getEcoHandler();

    Set<RandomLocationValidator> getValidatorSet();

    boolean setupEconomy();

    RandomConfigHandler getConfigHandler();

    WorldQueue getWorldQueue();

    LocationFactory getLocationFactory();
    //DeathTracker getDeathTracker();

    T getInstance();

    File getDataFolder();

}
