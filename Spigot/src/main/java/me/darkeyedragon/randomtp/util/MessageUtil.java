package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.RandomTeleport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, String message) {
        sendMessage(plugin, sender, MiniMessage.get().parse(message));
    }

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, Component component) {
        if (MiniMessage.get().serialize(component).isEmpty()) return;
        plugin.getBukkitAudience().audience(sender).sendMessage(component);
    }
}
