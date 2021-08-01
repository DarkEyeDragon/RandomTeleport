package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;

import java.util.HashMap;
import java.util.UUID;

public class CommonCooldownHandler implements CooldownHandler {

    private final HashMap<UUID, RandomCooldown> cooldowns;

    public CommonCooldownHandler() {
        this.cooldowns = new HashMap<>();
    }

    public CommonCooldownHandler(HashMap<UUID, RandomCooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    @Override
    public RandomCooldown getCooldown(RandomPlayer randomPlayer) {
        return getCooldown(randomPlayer.getUniqueId());
    }

    @Override
    public RandomCooldown getCooldown(UUID playerUUID) {
        return cooldowns.get(playerUUID);
    }

    @Override
    public RandomCooldown removeCooldown(RandomPlayer randomPlayer) {
        return removeCooldown(randomPlayer.getUniqueId());
    }

    @Override
    public RandomCooldown removeCooldown(UUID playerUUID) {
        return cooldowns.remove(playerUUID);
    }

    @Override
    public RandomCooldown addCooldown(RandomPlayer player, RandomCooldown randomCooldown) {
        return cooldowns.put(player.getUniqueId(), randomCooldown);
    }
}
