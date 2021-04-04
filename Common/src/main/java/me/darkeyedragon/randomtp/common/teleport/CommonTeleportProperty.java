package me.darkeyedragon.randomtp.common.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.teleport.RandomParticle;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public class CommonTeleportProperty implements TeleportProperty {

    private RandomLocation location;
    private final CommandIssuer commandIssuer;
    private final RandomPlayer target;
    private final boolean bypassEco;
    private final boolean bypassTeleportDelay;
    private final boolean bypassCooldown;
    private final RandomParticle<?> particle;

    public CommonTeleportProperty(RandomLocation location, CommandIssuer commandIssuer, RandomPlayer target, boolean bypassEco, boolean bypassTeleportDelay, boolean bypassCooldown, RandomParticle<?> particle) {
        this.location = location;
        this.commandIssuer = commandIssuer;
        this.target = target;
        this.bypassEco = bypassEco;
        this.bypassTeleportDelay = bypassTeleportDelay;
        this.bypassCooldown = bypassCooldown;
        this.particle = particle;
    }

    @Override
    public RandomLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(RandomLocation location) {
        this.location = location;
    }

    @Override
    public CommandIssuer getCommandIssuer() {
        return commandIssuer;
    }

    @Override
    public RandomPlayer getTarget() {
        return target;
    }

    @Override
    public boolean isBypassEco() {
        return bypassEco;
    }

    @Override
    public boolean isBypassTeleportDelay() {
        return bypassTeleportDelay;
    }

    @Override
    public boolean isBypassCooldown() {
        return bypassCooldown;
    }

    @Override
    public RandomParticle<?> getParticle() {
        return particle;
    }

    @Override
    public RandomWorld getWorld() {
        return location.getWorld();
    }
}
