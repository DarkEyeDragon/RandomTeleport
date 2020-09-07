package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;

public class PluginLoadListener implements Listener {

    private final RandomTeleport plugin;

    public PluginLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        new ArrayList<>(plugin.getValidatorList()).replaceAll(chunkValidator -> {
            if (chunkValidator.getName().equalsIgnoreCase(event.getPlugin().getName()) && !chunkValidator.isLoaded()) {
                chunkValidator.load();
                plugin.getLogger().info(ChatColor.GREEN + event.getPlugin().getName() + " as validator.");
                return chunkValidator;
            }
            return chunkValidator;
        });
    }
}
