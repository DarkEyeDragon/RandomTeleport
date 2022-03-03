package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.config.section.subsection.CommonSubSectionEconomy;
import me.darkeyedragon.randomtp.common.util.ComponentUtil;
import me.darkeyedragon.randomtp.common.util.CustomTime;
import me.darkeyedragon.randomtp.common.util.TimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

    private CommonSubSectionEconomy economy;

    @Override
    public Component getInitTeleport() {
        return ComponentUtil.toComponent(initTeleport);
    }

    @Override
    public Component getInitTeleportDelay(long ticks) {
        CustomTime customTime = TimeUtil.formatTime(ticks);
        TagResolver.Single time = Placeholder.unparsed("time", customTime.toFormattedString());
        return ComponentUtil.toComponent(initTeleportDelay, time);
    }

    @Override
    public Component getTeleportCanceled() {
        return ComponentUtil.toComponent(teleportCanceled);
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        TagResolver.Single posX = Placeholder.unparsed("x", location.getBlockX() + "");
        TagResolver.Single posY = Placeholder.unparsed("y", location.getBlockY() + "");
        TagResolver.Single posZ = Placeholder.unparsed("z", location.getBlockZ() + "");
        return ComponentUtil.toComponent(teleport, posX, posY, posZ);
    }

    @Override
    public Component getCountdown(long remainingTicks) {
        CustomTime customTime = TimeUtil.formatTime(remainingTicks);
        TagResolver.Single time = Placeholder.unparsed("time", customTime.toFormattedString());
        return ComponentUtil.toComponent(countdown, time);
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        TagResolver.Single worldName = Placeholder.unparsed("world", world.getName());
        return ComponentUtil.toComponent(noWorldPermission, worldName);
    }

    @Override
    public Component getEmptyQueue() {
        return ComponentUtil.toComponent(depletedQueue);
    }

    @Override
    public SubSectionEconomy getSubSectionEconomy() {
        return economy;
    }

    @Override
    public SubSectionSign getSubSectionSign() {
        return null;
    }

    @Override
    public Component getInvalidDefaultWorld(String worldName) {
        return ComponentUtil.toComponent(invalidDefaultWorld);
    }
}
