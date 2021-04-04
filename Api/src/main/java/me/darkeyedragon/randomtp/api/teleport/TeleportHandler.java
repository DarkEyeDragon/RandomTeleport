package me.darkeyedragon.randomtp.api.teleport;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;

public interface TeleportHandler {

    TeleportResponse toRandomLocation(RandomPlayer randomPlayer);

}
