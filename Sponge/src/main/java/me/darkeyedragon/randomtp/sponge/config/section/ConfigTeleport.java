package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;
import me.darkeyedragon.randomtp.common.util.TimeUtil;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigTeleport implements SectionTeleport {

    @Setting
    private String cooldown;
    @Setting
    private String delay;
    @Setting
    private boolean cancel_on_move;
    @Setting
    private String death_timer;

    @Override
    public long getCooldown() {
        return TimeUtil.stringToLong(cooldown);
    }

    @Override
    public long getDelay() {
        return TimeUtil.stringToLong(delay);
    }

    @Override
    public boolean isCancelOnMove() {
        return cancel_on_move;
    }

    @Override
    public long getDeathTimer() {
        return TimeUtil.stringToLong(death_timer);
    }

    @Override
    //TODO implement
    public TeleportParticle getParticle() {
        return null;
    }
}
