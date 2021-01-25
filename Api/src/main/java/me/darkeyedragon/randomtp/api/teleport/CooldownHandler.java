package me.darkeyedragon.randomtp.api.teleport;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;

import java.util.UUID;

public interface CooldownHandler {
    RandomCooldown getCooldown(RandomPlayer randomPlayer);

    RandomCooldown getCooldown(UUID playerUUID);

    boolean removeCooldown(RandomPlayer randomPlayer);

    boolean removeCooldown(UUID playerUUID);

    boolean addCooldown(RandomCooldown randomCooldown);
}
