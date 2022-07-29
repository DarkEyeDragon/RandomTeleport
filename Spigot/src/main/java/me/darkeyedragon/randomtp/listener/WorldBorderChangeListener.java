package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.event.Listener;

public class WorldBorderChangeListener implements Listener {

    public final RandomTeleport plugin;

    public WorldBorderChangeListener(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    /*
     * When the worldborder finishes changing we want to regenerate the safe locations
     * to prevent people rtp'ing outside of the world border.
     * However this should only happen for worlds where the use_worldborder config setting is true.
     * */
    /*@EventHandler
    public void onWorldBorderChange(WorldBorderBoundsChangeFinishEvent event) {
        plugin.getLogger().info("WorldBorder changed. Updating worlds that rely on it...");
        RandomConfigHandler configHandler = plugin.getConfigHandler();
        RandomWorld world = WorldUtil.toRandomWorld(event.getWorld());
        ConfigWorld configWorld = configHandler.getSectionWorld().getConfigWorld(world.getName());
        if (configWorld.isUseWorldborder()) {
            WorldQueue worldQueue = plugin.getWorldHandler().getWorldQueue();
            worldQueue.remove(world);
            worldQueue.put(
                    world, new LocationQueue(
                            plugin, configHandler.getSectionQueue().getSize(),
                            WorldHandler.getLocationSearcher(world.getEnvironment())
                    )
            );
            plugin.getWorldHandler().populateWorld(configWorld);
        }
    }*/
}
