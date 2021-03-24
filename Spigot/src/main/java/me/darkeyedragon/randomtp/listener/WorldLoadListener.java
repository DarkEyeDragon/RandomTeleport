package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.event.Listener;

public class WorldLoadListener implements Listener {

    //private final BukkitConfigHandler bukkitConfigHandler;
    private final RandomTeleport plugin;

    public WorldLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
        //this.bukkitConfigHandler = plugin.getConfigHandler();
    }

    /*@EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        if (bukkitConfigHandler.getSectionWorld().getWorlds().contains(world)) {
            //Add a new world to the world queue and generate random locations
            LocationQueue locationQueue = new LocationQueue(bukkitConfigHandler.getSectionQueue().getSize(), LocationSearcherFactory.getLocationSearcher(world, plugin));
            //Subscribe to the locationqueue to be notified of changes
            if (bukkitConfigHandler.getSectionDebug().isShowQueuePopulation()) {
                int size = bukkitConfigHandler.getSectionQueue().getSize();
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
    }*/
}
