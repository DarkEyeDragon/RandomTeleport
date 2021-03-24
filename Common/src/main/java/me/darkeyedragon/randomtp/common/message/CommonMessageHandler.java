package me.darkeyedragon.randomtp.common.message;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
        Validate.notNull(textToTranslate, "Cannot translate null text");
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }
}
