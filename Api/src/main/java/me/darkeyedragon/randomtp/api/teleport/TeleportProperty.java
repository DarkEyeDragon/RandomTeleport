package me.darkeyedragon.randomtp.api.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface TeleportProperty {

    RandomLocation getLocation();

    CommandIssuer getCommandIssuer();

    RandomPlayer getTarget();

    boolean isBypassEco();

    boolean isBypassTeleportDelay();

    boolean isBypassCooldown();

    RandomParticle<?> getParticle();

    RandomWorld getWorld();
}
