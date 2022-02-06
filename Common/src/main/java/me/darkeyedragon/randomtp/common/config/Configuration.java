package me.darkeyedragon.randomtp.common.config;

import me.darkeyedragon.randomtp.common.config.section.CommonSectionBlacklist;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionDebug;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionEconomy;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionMessage;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionQueue;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionTeleport;
import me.darkeyedragon.randomtp.common.config.section.CommonSectionWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Configuration {

    //TODO check if interface can be used here. Currently the serializers rely on the common impl

    private CommonSectionMessage message;
    private CommonSectionQueue queue;
    private CommonSectionWorld worlds;
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

    public CommonSectionWorld getWorlds() {
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
