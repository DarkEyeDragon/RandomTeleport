package me.darkeyedragon.randomtp.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class ComponentUtil {

    public static MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component toComponent(String message) {
        return miniMessage.deserialize(message);
    }

    public static Component toComponent(String message, TagResolver... tagResolvers) {
        return miniMessage.deserialize(message, tagResolvers);
    }
}
