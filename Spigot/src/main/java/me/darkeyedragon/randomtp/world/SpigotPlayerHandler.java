package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotPlayerHandler implements PlayerHandler {
    @Override
    @Nullable
    public RandomPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return getPlayer(player);
    }

    @Override
    @Nullable
    public RandomPlayer getPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        return getPlayer(player);
    }

    private RandomPlayer getPlayer(Player player) {
        if (player == null) return null;
        return new PlayerSpigot(player);
    }
}
