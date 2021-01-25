package me.darkeyedragon.randomtp.teleport;

import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;

import java.util.HashMap;
import java.util.UUID;

public class SpigotCooldownHandler implements CooldownHandler {

    private final HashMap<UUID, RandomCooldown> cooldowns;

    public SpigotCooldownHandler() {
        this.cooldowns = new HashMap<>();
    }

    public SpigotCooldownHandler(HashMap<UUID, RandomCooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    @Override
    public RandomCooldown getCooldown(RandomPlayer randomPlayer) {
        UUID uuid = randomPlayer.getUniqueId();
        return getCooldown(uuid);
    }

    @Override
    public RandomCooldown getCooldown(UUID playerUUID) {
        return cooldowns.get(playerUUID);
    }
}
