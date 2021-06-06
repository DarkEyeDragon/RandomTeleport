package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigMessage implements SectionMessage {

    @Setting
    String initteleport;
    @Setting
    String initteleport_delay;
    @Setting
    String teleport_canceled;
    @Setting
    String teleport;
    @Setting
    String depleted_queue;
    @Setting
    String countdown;
    @Setting
    String no_world_permission;
    @Setting
    String empty_queue;
    @Setting
    String economy;
    @Setting
    String insufficient_funds;
    @Setting
    String payment;

    @Override
    public Component getInitTeleport() {
        return null;
    }

    @Override
    public Component getInitTeleportDelay(long millis) {
        return null;
    }

    @Override
    public Component getTeleportCanceled() {
        return null;
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        return null;
    }

    @Override
    public Component getDepletedQueue() {
        return null;
    }

    @Override
    public Component getCountdown(long remainingTicks) {
        return null;
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        return null;
    }

    @Override
    public Component getEmptyQueue() {
        return null;
    }

    @Override
    public SubSectionEconomy getSubSectionEconomy() {
        return null;
    }

    @Override
    public SubSectionSign getSubSectionSign() {
        return null;
    }
}
