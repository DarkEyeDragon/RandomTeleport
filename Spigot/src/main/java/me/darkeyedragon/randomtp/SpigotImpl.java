package me.darkeyedragon.randomtp;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotImpl extends JavaPlugin {

    private RandomTeleport randomTeleport;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        randomTeleport = new RandomTeleport(this);
        randomTeleport.init();
    }

    public RandomTeleport getInstance() {
        return randomTeleport;
    }

    @Override
    public void onDisable() {

    }
}
