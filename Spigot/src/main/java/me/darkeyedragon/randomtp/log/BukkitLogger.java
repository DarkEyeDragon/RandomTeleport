package me.darkeyedragon.randomtp.log;

import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import org.bukkit.Bukkit;

public class BukkitLogger implements PluginLogger {



    @Override
    public void info(String s) {
        Bukkit.getLogger().info(s);
    }

    @Override
    public void warn(String s) {
        Bukkit.getLogger().warning(s);
    }

    @Override
    public void severe(String s) {
        Bukkit.getLogger().severe(s);
    }
}
