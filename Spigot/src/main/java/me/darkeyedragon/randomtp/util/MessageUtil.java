package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.RandomTeleport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, String message) {
        if (message.isEmpty()) return;

        sendMessage(plugin, sender, MiniMessage.get().parse(message));
    }

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, Component component) {
        plugin.getBukkitAudience().sender(sender).sendMessage(component);
    }
}
