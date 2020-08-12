package me.darkeyedragon.randomtp.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private final static Pattern pattern = Pattern.compile("\\[(§x)([A-Za-z0-9]{3,6})\\+(.+?)]");

    public static TextComponent getChatComponents(String message) {
        Matcher matcher = pattern.matcher(message);
        TextComponent textComponent = new TextComponent();
        int lastEnd = 0;
        while (matcher.find()) {
            int s = matcher.start();
            int e = matcher.end();
            if (lastEnd != s) {
                textComponent.addExtra(message.substring(lastEnd, s));
            }
            String hex = matcher.group(2);
            String text = matcher.group(3);
            TextComponent addTextComponent = new TextComponent(text);
            addTextComponent.setColor(ChatColor.of("#" + hex));
            textComponent.addExtra(addTextComponent);
            lastEnd = e;
        }
        if (lastEnd != message.length() - 1) {
            textComponent.addExtra(message.substring(lastEnd));
        }
        return textComponent;
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (message.isEmpty()) return;
        sender.sendMessage(message);
    }

    public static void sendMessage(CommandSender sender, BaseComponent baseComponent) {
        if (baseComponent.toPlainText().isEmpty()) return;
        sender.spigot().sendMessage(baseComponent);
    }

}
