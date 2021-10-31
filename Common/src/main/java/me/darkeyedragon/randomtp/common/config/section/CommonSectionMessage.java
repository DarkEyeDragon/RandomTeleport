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

    private CommonSubSectionEconomy economy;

    @Override
    public Component getInitTeleport() {
        return ComponentUtil.toComponent(initTeleport);
    }

    @Override
    public Component getInitTeleportDelay(long ticks) {
        CustomTime customTime = TimeUtil.formatTime(ticks);
        return ComponentUtil.toComponent(initTeleportDelay, Template.of("time", customTime.toFormattedString()));
    }

    @Override
    public Component getTeleportCanceled() {
        return ComponentUtil.toComponent(teleportCanceled);
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        return ComponentUtil.toComponent(teleport, Template.of("posX", location.getBlockX() + ""), Template.of("posY", location.getBlockY() + ""), Template.of("posZ", location.getBlockZ() + ""));
    }

    @Override
    public Component getCountdown(long remainingTicks) {
        CustomTime customTime = TimeUtil.formatTime(remainingTicks);
        return ComponentUtil.toComponent(countdown, Template.of("time", customTime.toFormattedString()));
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        return ComponentUtil.toComponent(noWorldPermission, Template.of("world", world.getName()));
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
