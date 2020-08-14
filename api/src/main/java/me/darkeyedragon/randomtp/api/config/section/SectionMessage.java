package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;

public interface SectionMessage {
    SubSectionEconomy getEconomySection();
    String getInitTeleport();
    String getInitTeleportDelay();
    String getTeleportCanceled();
    String getTeleport();
    String getDepletedQueue();
    String getCountdown();
    String getNoWorldPermission();
    String getEmptyQueue();

}
