package me.darkeyedragon.randomtp.common.message;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.util.ComponentUtil;
import net.kyori.adventure.text.Component;

public class CommonMessageHandler implements MessageHandler {


    private final RandomTeleportPlugin<?> plugin;

    public CommonMessageHandler(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(RandomPlayer randomPlayer, Component component) {
        if (ComponentUtil.miniMessage.serialize(component).isEmpty()) return;
        plugin.getAudience().player(randomPlayer.getUniqueId()).sendMessage(component);
    }

    @Override
    public void sendMessage(RandomPlayer randomPlayer, String component) {
        sendMessage(randomPlayer, ComponentUtil.miniMessage.deserialize(component));
    }

    @Override
    public void sendMessage(CommandIssuer commandIssuer, Component component) {
        if (ComponentUtil.miniMessage.serialize(component).isEmpty()) return;
        if (commandIssuer.isPlayer()) {
            plugin.getAudience().player(commandIssuer.getUniqueId()).sendMessage(component);
        } else {
            plugin.getAudience().console().sendMessage(component);
        }
    }

    @Override
    public void sendMessage(CommandIssuer commandIssuer, String message) {
        sendMessage(commandIssuer, ComponentUtil.miniMessage.deserialize(message));
    }
}
