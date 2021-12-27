package me.darkeyedragon.randomtp.api.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

public interface TeleportProperty {

    long getInitTime();

    CommandIssuer getCommandIssuer();

    RandomPlayer getTarget();

    boolean isBypassEco();

    boolean isBypassTeleportDelay();

    boolean isBypassCooldown();

    RandomParticle getParticle();

    RandomWorld getWorld();

    double getPrice();

    long getDelay();

    boolean getCancelOnMove();
}
