package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.validator.ValidatorFactory;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginLoadListener implements Listener {

    private final RandomTeleport plugin;

    public PluginLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        plugin.getValidatorList().replaceAll(chunkValidator -> {
            if (chunkValidator.getValidator().getName().equalsIgnoreCase(event.getPlugin().getName()) && !chunkValidator.isLoaded()) {
                plugin.getLogger().info(ChatColor.GREEN + event.getPlugin().getName() + " as validator.");
                return ValidatorFactory.createFrom(chunkValidator.getValidator());
            } else {
                return chunkValidator;
            }
        });
    }
}
