package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.world.LocationQueue;
import me.darkeyedragon.randomtp.world.QueueListener;
import me.darkeyedragon.randomtp.world.location.LocationSearcherFactory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    private final ConfigHandler configHandler;
    private final RandomTeleport plugin;

    public WorldLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        if (configHandler.getConfigWorld().getWorlds().contains(world)) {
            //Add a new world to the world queue and generate random locations
            LocationQueue locationQueue = new LocationQueue(configHandler.getConfigQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, plugin));
            //Subscribe to the locationqueue to be notified of changes
            if (configHandler.getConfigDebug().isShowQueuePopulation()) {
                int size = configHandler.getConfigQueue().getSize();
                locationQueue.subscribe(new QueueListener<Location>() {
                    @Override
                    public void onAdd(Location element) {
                        plugin.getLogger().info("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                    }

                    @Override
                    public void onRemove(Location element) {
                        plugin.getLogger().info("Safe location consumed for " + world.getName() + " (" + locationQueue.size() + "/" + size + ")");
                    }
                });
            }
            locationQueue.generate(plugin.getLocationFactory().getWorldConfigSection(world));
            plugin.getWorldQueue().put(world, locationQueue);
        }
    }
}
