package me.darkeyedragon.randomtp.log;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class BukkitLogger implements PluginLogger {

    private final RandomTeleport plugin;

    public BukkitLogger(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public void info(String s) {
        Bukkit.getLogger().info(s);
    }

    @Override
    public void info(final Component component) {
        Component finalComponent = Component.text(PREFIX).append(component);
        plugin.getBukkitAudience().console().sendMessage(Identity.nil(), finalComponent);
    }

    @Override
    public void warn(String s) {
        Bukkit.getLogger().warning(s);
    }

    @Override
    public void warn(Component component) {
        Component finalComponent = Component.text(PREFIX).append(component);
        plugin.getBukkitAudience().console().sendMessage(Identity.nil(), finalComponent);    }

    @Override
    public void severe(String s) {
        Bukkit.getLogger().severe(s);
    }

    @Override
    public void severe(Component component) {
        Component finalComponent = Component.text(PREFIX).append(component);
        plugin.getBukkitAudience().console().sendMessage(Identity.nil(), finalComponent);
    }
}
