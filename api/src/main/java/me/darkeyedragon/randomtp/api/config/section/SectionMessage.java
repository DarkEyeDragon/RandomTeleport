package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface SectionMessage {
    String getInitTeleport();
    String getInitTeleportDelay(long millis);
    String getTeleportCanceled();
    String getTeleport(RandomLocation location);
    String getDepletedQueue();
    String getCountdown(long remaining);
    String getNoWorldPermission(RandomWorld world);
    String getEmptyQueue();
    SubSectionEconomy getSubSectionEconomy();
}
