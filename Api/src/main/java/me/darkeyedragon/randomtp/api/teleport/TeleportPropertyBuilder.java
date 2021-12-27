package me.darkeyedragon.randomtp.api.teleport;

import co.aikar.commands.CommandIssuer;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

public interface TeleportPropertyBuilder {

    TeleportPropertyBuilder commandIssuer(CommandIssuer commandIssuer);

    TeleportPropertyBuilder target(RandomPlayer target);

    TeleportPropertyBuilder price(double price);

    TeleportPropertyBuilder bypassEco(boolean bypassEco);

    TeleportPropertyBuilder bypassTeleportDelay(boolean bypassTeleportDelay);

    TeleportPropertyBuilder bypassCooldown(boolean bypassCooldown);

    TeleportPropertyBuilder particle(RandomParticle particle);

    TeleportPropertyBuilder initTime(long initTime);

    TeleportPropertyBuilder delay(long delay);

    TeleportPropertyBuilder cancelOnMove(boolean cancelOnMove);

    TeleportProperty build();
}
