package me.darkeyedragon.randomtp.api.world;

import java.util.UUID;

public interface PlayerHandler {
    RandomPlayer getPlayer(UUID uuid);

    RandomPlayer getPlayer(String name);
}
