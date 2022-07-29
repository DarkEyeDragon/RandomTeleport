package me.darkeyedragon.randomtp.api.teleport;

import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CooldownHandler {
    @Nullable RandomCooldown getCooldown(RandomPlayer randomPlayer);

    @Nullable RandomCooldown getCooldown(UUID playerUUID);

    RandomCooldown removeCooldown(RandomPlayer randomPlayer);

    RandomCooldown removeCooldown(UUID playerUUID);

    RandomCooldown addCooldown(RandomPlayer player, RandomCooldown randomCooldown);
}
