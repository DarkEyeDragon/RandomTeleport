package me.darkeyedragon.randomtp.common.message;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CommonMessageHandler implements MessageHandler {


    private final RandomTeleportPlugin<?> plugin;

    public CommonMessageHandler(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(RandomPlayer randomPlayer, Component component) {
        if (MiniMessage.get().serialize(component).isEmpty()) return;
        plugin.getAudience().player(randomPlayer.getUniqueId()).sendMessage(component);
    }

    @Override
    public void sendMessage(RandomPlayer randomPlayer, String component) {
        sendMessage(randomPlayer, MiniMessage.get().parse(component));
    }

    @Override
    public void sendMessage(CommandIssuer commandIssuer, Component component) {
        if (MiniMessage.get().serialize(component).isEmpty()) return;
        if (commandIssuer.isPlayer()) {
            plugin.getAudience().player(commandIssuer.getUniqueId()).sendMessage(component);
        } else {
            plugin.getAudience().console().sendMessage(component);
        }
    }

    @Override
    public void sendMessage(CommandIssuer commandIssuer, String message) {
        sendMessage(commandIssuer, MiniMessage.get().parse(message));
    }
}
