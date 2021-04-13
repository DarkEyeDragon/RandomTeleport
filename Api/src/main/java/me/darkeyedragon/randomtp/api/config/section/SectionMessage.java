package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import net.kyori.adventure.text.Component;

public interface SectionMessage {
    Component getInitTeleport();

    Component getInitTeleportDelay(long millis);

    Component getTeleportCanceled();

    Component getTeleport(RandomLocation location);

    Component getCountdown(long remaining);

    Component getNoWorldPermission(RandomWorld world);

    Component getEmptyQueue();

    SubSectionEconomy getSubSectionEconomy();

    SubSectionSign getSubSectionSign();

    Component getInvalidDefaultWorld(String worldName);
}
