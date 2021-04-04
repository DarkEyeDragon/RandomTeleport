package me.darkeyedragon.randomtp;

import me.darkeyedragon.randomtp.stat.BStats;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotImpl extends JavaPlugin {

    private RandomTeleport randomTeleport;
    Metrics metrics;
    BStats bStats;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        metrics = new Metrics(this, 8852);
        bStats = new BStats();
        metrics.addCustomChart(new SingleLineChart("random_teleports", () -> bStats.getRandomTeleportStats()));
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
