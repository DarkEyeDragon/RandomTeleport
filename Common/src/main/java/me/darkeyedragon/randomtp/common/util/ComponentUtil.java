package me.darkeyedragon.randomtp.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ComponentUtil {

    public static Component toComponent(String message) {
        return MiniMessage.get().parse(message);
    }

    public static Component toComponent(String message, Template... template) {
        return MiniMessage.get().parse(message, template);
    }
}
