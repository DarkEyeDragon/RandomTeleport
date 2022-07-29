package me.darkeyedragon.randomtp.api.message;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import net.kyori.adventure.text.Component;

public interface MessageHandler {

    void sendMessage(RandomPlayer randomPlayer, Component component);

    void sendMessage(RandomPlayer randomPlayer, String component);

    void sendMessage(CommandIssuer commandIssuer, Component component);

    void sendMessage(CommandIssuer commandIssuer, String message);

    void sendDebugMessage(String message);

    void sendDebugMessage(Component message);
}
