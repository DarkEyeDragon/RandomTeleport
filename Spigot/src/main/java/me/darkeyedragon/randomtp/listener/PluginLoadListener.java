package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.validator.Validator;
import me.darkeyedragon.randomtp.validator.ValidatorFactory;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PluginLoadListener implements Listener {

    private final RandomTeleport plugin;

    public PluginLoadListener(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        new ArrayList<>(plugin.getValidatorList()).replaceAll(chunkValidator -> {
            if (chunkValidator.getValidator().getName().equalsIgnoreCase(event.getPlugin().getName()) && !chunkValidator.isLoaded()) {
                plugin.getLogger().info(ChatColor.GREEN + event.getPlugin().getName() + " as validator.");
                return ValidatorFactory.createFrom((Validator) chunkValidator.getValidator());
            } else {
                return chunkValidator;
            }
        });
    }
}
