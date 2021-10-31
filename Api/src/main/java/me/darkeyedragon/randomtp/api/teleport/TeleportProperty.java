package me.darkeyedragon.randomtp.api.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface TeleportProperty {

    long getInitTime();

    RandomLocation getLocation();

    void setLocation(RandomLocation location);

    CommandIssuer getCommandIssuer();

    RandomPlayer getTarget();

    boolean isBypassEco();

    boolean isBypassTeleportDelay();

    boolean isBypassCooldown();

    RandomParticle getParticle();

    RandomWorld getWorld();

    double getPrice();
}
