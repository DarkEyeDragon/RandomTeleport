package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionMessage implements SectionMessage {

    private Component initTeleport;
    private Component initTeleportDelay;
    private Component teleportCanceled;
    private Component teleport;
    private Component countdown;
    private Component noWorldPermission;
    private Component emptyQueue;
    private Component invalidDefaultWorld;

    @Override
    public Component getInitTeleport() {
        return initTeleport;
    }

    @Override
    public Component getInitTeleportDelay(long millis) {
        return initTeleportDelay;
    }

    @Override
    public Component getTeleportCanceled() {
        return teleportCanceled;
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        return teleport;
    }

    @Override
    public Component getCountdown(long remaining) {
        return countdown;
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        return noWorldPermission;
    }

    @Override
    public Component getEmptyQueue() {
        return emptyQueue;
    }

    @Override
    public SubSectionEconomy getSubSectionEconomy() {
        //TODO
        return null;
    }

    @Override
    public SubSectionSign getSubSectionSign() {
        //TODO
        return null;
    }

    @Override
    public Component getInvalidDefaultWorld(String worldName) {
        return invalidDefaultWorld;
    }
}
