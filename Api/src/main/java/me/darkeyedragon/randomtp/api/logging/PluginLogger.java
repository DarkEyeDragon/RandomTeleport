package me.darkeyedragon.randomtp.api.logging;

import net.kyori.adventure.text.Component;

public interface PluginLogger {

    String PREFIX = "[RandomTeleport] ";

    void info(String s);
    void info(Component component);

    void warn(String s);
    void warn(Component component);

    void severe(String s);
    void severe(Component component);

}
