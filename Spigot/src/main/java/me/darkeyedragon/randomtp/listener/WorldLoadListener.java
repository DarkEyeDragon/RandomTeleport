package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    private final RandomConfigHandler configHandler;
    private final RandomTeleport plugin;

    public WorldLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        RandomWorld randomworld = WorldUtil.toRandomWorld(world);
        configHandler.populateWorldConfigSection();
        if (configHandler.getSectionWorld().getWorlds().contains(randomworld)) {
            plugin.getLogger().info(ChatColor.GOLD + "World load detected for " + world.getName() + "! Starting generation of worlds...");
            plugin.getWorldHandler().populateWorld(randomworld);
        }
    }
}
