package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;

public class ConfigTeleport implements SectionTeleport {
    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public long getDelay() {
        return 0;
    }

    @Override
    public boolean isCancelOnMove() {
        return false;
    }

    @Override
    public long getDeathTimer() {
        return 0;
    }
}
