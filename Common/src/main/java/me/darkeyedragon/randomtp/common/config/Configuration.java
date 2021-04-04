package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.common.config.section.*;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Configuration {

    private CommonSectionMessage message;
    private CommonSectionQueue queue;
    private CommonSectionWorldHolder worlds;
    private CommonSectionTeleport teleport;
    private CommonSectionDebug debug;
    private CommonSectionEconomy economy;
    private CommonSectionBlacklist blacklist;

    public CommonSectionMessage getMessages() {
        return message;
    }

    public CommonSectionQueue getQueue() {
        return queue;
    }

    public CommonSectionWorldHolder getWorlds() {
        return worlds;
    }

    public CommonSectionTeleport getTeleport() {
        return teleport;
    }

    public CommonSectionDebug getDebug() {
        return debug;
    }

    public CommonSectionEconomy getEconomy() {
        return economy;
    }

    public CommonSectionBlacklist getBlacklist() {
        return blacklist;
    }
}
