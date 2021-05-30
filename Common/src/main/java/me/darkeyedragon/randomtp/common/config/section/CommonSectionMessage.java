package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionMessage implements SectionMessage {

    private String initTeleport;
    private String initTeleportDelay;
    private String teleportCanceled;
    private String teleport;
    private String countdown;
    private String noWorldPermission;
    private String depletedQueue;
    private String invalidDefaultWorld;

    @Override
    public Component getInitTeleport() {
        return toComponent(initTeleport);
    }

    @Override
    public Component getInitTeleportDelay(long millis) {
        return toComponent(initTeleportDelay);
    }

    @Override
    public Component getTeleportCanceled() {
        return toComponent(teleportCanceled);
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        return toComponent(teleport, Template.of("posX", location.getBlockX() + ""), Template.of("posY", location.getBlockY() + ""), Template.of("posZ", location.getBlockZ() + ""));
    }

    @Override
    public Component getCountdown(long remaining) {
        return toComponent(countdown, Template.of("time", remaining + ""));
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        return toComponent(noWorldPermission, Template.of("world", world.getName()));
    }

    @Override
    public Component getEmptyQueue() {
        return toComponent(depletedQueue);
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
        return toComponent(invalidDefaultWorld);
    }

    private Component toComponent(String message) {
        return MiniMessage.get().parse(message);
    }

    private Component toComponent(String message, Template... template) {
        return MiniMessage.get().parse(message, template);
    }
}
