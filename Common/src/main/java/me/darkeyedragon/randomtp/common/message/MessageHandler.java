package me.darkeyedragon.randomtp.common.message;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageHandler {

    public static void sendMessage(RandomTeleportPlugin<?> plugin, CommandIssuer sender, String message) {
        sendMessage(plugin, sender.getIssuer(), MiniMessage.get().parse(message));
    }

    public static void sendMessage(RandomTeleportPlugin<?> plugin, CommandIssuer sender, Component component) {
        if (MiniMessage.get().serialize(component).isEmpty()) return;
        if (sender.isPlayer()) {
            plugin.getAudience().console().sendMessage(component);
        } else
            plugin.getAudience().player(sender.getUniqueId()).sendMessage(component);
    }
}
