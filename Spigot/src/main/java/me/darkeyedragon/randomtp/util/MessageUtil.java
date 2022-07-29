package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.common.util.ComponentUtil;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

//TODO look into using or removing
public class MessageUtil {

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, String message) {
        sendMessage(plugin, sender, ComponentUtil.miniMessage.deserialize(message));
    }

    public static void sendMessage(RandomTeleport plugin, CommandSender sender, Component component) {
        if (ComponentUtil.miniMessage.serialize(component).isEmpty()) return;
        plugin.getBukkitAudience().sender(sender).sendMessage(Identity.nil() ,component);
    }
}
