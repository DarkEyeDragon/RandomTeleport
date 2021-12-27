package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;

import java.util.UUID;

public interface PlayerHandler {
    RandomPlayer getPlayer(UUID uuid);
    RandomPlayer getPlayer(String name);
}
