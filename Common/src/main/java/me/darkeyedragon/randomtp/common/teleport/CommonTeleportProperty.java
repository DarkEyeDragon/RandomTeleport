package me.darkeyedragon.randomtp.common.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

public class CommonTeleportProperty implements TeleportProperty {

    private final CommandIssuer commandIssuer;
    private final RandomPlayer target;
    private final RandomWorld world;
    private final boolean bypassEco;
    private final boolean bypassTeleportDelay;
    private final boolean bypassCooldown;
    private final RandomParticle particle;
    private final double price;
    private final long initTime;
    private final long delay;
    private final boolean cancelOnMove;

    protected CommonTeleportProperty(CommandIssuer commandIssuer, RandomPlayer target, RandomWorld world, double price, boolean bypassEco, boolean bypassTeleportDelay, boolean bypassCooldown, RandomParticle particle, long initTime, long delay, boolean cancelOnMove) {
        this.commandIssuer = commandIssuer;
        this.target = target;
        this.world = world;
        this.bypassEco = bypassEco;
        this.price = price;
        this.bypassTeleportDelay = bypassTeleportDelay;
        this.bypassCooldown = bypassCooldown;
        this.particle = particle;
        this.initTime = initTime;
        this.delay = delay;
        this.cancelOnMove = cancelOnMove;
    }

    @Override
    public long getInitTime() {
        return initTime;
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
    public RandomParticle getParticle() {
        return particle;
    }

    @Override
    public RandomWorld getWorld() {
        return world;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public boolean getCancelOnMove() {
        return cancelOnMove;
    }
}
