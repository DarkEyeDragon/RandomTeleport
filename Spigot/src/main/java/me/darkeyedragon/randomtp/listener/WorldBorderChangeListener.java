package me.darkeyedragon.randomtp.listener;

import io.papermc.paper.event.world.border.WorldBorderBoundsChangeFinishEvent;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.event.EventHandler;
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
    @EventHandler
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
                            LocationSearcherFactory.getLocationSearcher(world, plugin)
                    )
            );
            plugin.getWorldHandler().populateWorld(configWorld);
        }
    }
}
