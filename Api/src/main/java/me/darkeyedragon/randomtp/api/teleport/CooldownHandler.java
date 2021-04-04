package me.darkeyedragon.randomtp.api.teleport;

import me.darkeyedragon.randomtp.api.world.RandomPlayer;

import java.util.UUID;

public interface CooldownHandler {
    RandomCooldown getCooldown(RandomPlayer randomPlayer);

    RandomCooldown getCooldown(UUID playerUUID);

    RandomCooldown removeCooldown(RandomPlayer randomPlayer);

    RandomCooldown removeCooldown(UUID playerUUID);

    RandomCooldown addCooldown(RandomPlayer player, RandomCooldown randomCooldown);
}
