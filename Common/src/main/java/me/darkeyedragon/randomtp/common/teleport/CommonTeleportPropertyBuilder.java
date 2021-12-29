package me.darkeyedragon.randomtp.common.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.teleport.TeleportPropertyBuilder;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

public class CommonTeleportPropertyBuilder implements TeleportPropertyBuilder {


    private CommandIssuer commandIssuer;
    private RandomPlayer target;
    private double price;
    private boolean bypassEco;
    private boolean bypassTeleportDelay;
    private boolean bypassCooldown;
    private RandomParticle particle;
    private long initTime;
    private long delay;
    private boolean cancelOnMove;
    private RandomWorld world;

    public TeleportPropertyBuilder world(RandomWorld world) {
        this.world = world;
        return this;
    }

    public TeleportPropertyBuilder commandIssuer(CommandIssuer commandIssuer) {
        this.commandIssuer = commandIssuer;
        return this;
    }

    public TeleportPropertyBuilder target(RandomPlayer target) {
        this.target = target;
        return this;
    }

    public TeleportPropertyBuilder price(double price) {
        this.price = price;
        return this;
    }

    public TeleportPropertyBuilder bypassEco(boolean bypassEco) {
        this.bypassEco = bypassEco;
        return this;
    }

    public TeleportPropertyBuilder bypassTeleportDelay(boolean bypassTeleportDelay) {
        this.bypassTeleportDelay = bypassTeleportDelay;
        return this;
    }

    public TeleportPropertyBuilder bypassCooldown(boolean bypassCooldown) {
        this.bypassCooldown = bypassCooldown;
        return this;
    }

    public TeleportPropertyBuilder particle(RandomParticle particle) {
        this.particle = particle;
        return this;
    }

    public TeleportPropertyBuilder initTime(long initTime) {
        this.initTime = initTime;
        return this;
    }

    @Override
    public TeleportPropertyBuilder delay(long delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public TeleportPropertyBuilder cancelOnMove(boolean cancelOnMove) {
        this.cancelOnMove = cancelOnMove;
        return this;
    }

    public TeleportProperty build() {
        return new CommonTeleportProperty(commandIssuer, target, world, price, bypassEco, bypassTeleportDelay, bypassCooldown, particle, initTime, delay, cancelOnMove);
    }
}
