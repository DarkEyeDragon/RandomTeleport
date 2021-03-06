package me.darkeyedragon.randomtp;

import me.darkeyedragon.randomtp.stat.BStats;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotImpl extends JavaPlugin {

    private RandomTeleport randomTeleport;
    Metrics metrics;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        metrics = new Metrics(this, 8852);
        metrics.addCustomChart(new Metrics.SingleLineChart("random_teleports", BStats::getRandomTeleportStats));
        randomTeleport = new RandomTeleport(this);
        randomTeleport.init();
    }

    public RandomTeleport getInstance() {
        return randomTeleport;
    }

    @Override
    public void onDisable() {

    }

    public Metrics getMetrics() {
        return metrics;
    }
}
