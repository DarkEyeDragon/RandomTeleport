package me.darkeyedragon.randomtp.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.teleport.RandomParticle;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import org.bukkit.Particle;

public class TeleportPropertySpigot implements TeleportProperty {

    private final CommandIssuer commandIssuer;
    private final RandomPlayer target;
    private final RandomLocation location;
    private final boolean bypassEco;
    private final boolean bypassTeleportDelay;
    private final boolean bypassCooldown;
    private final RandomParticle<Particle> particle;


    public TeleportPropertySpigot(CommandIssuer commandIssuer, RandomPlayer target, RandomLocation location, boolean bypassEco, boolean bypassTeleportDelay, boolean bypassCooldown, RandomParticle<Particle> particle) {
        this.commandIssuer = commandIssuer;
        this.target = target;
        this.location = location;
        this.bypassEco = bypassEco;
        this.bypassTeleportDelay = bypassTeleportDelay;
        this.bypassCooldown = bypassCooldown;
        this.particle = particle;
    }

    @Override
    public RandomLocation getLocation() {
        return location;
    }

    public CommandIssuer getCommandIssuer() {
        return commandIssuer;
    }

    @Override
    public RandomPlayer getTarget() {
        return target;
    }

    public boolean isBypassEco() {
        return bypassEco;
    }

    public boolean isBypassTeleportDelay() {
        return bypassTeleportDelay;
    }

    public boolean isBypassCooldown() {
        return bypassCooldown;
    }

    public RandomParticle<Particle> getParticle() {
        return particle;
    }
}
