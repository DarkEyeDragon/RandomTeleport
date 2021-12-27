package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SpigotPlayerHandler implements PlayerHandler {
    @Override
    public RandomPlayer getPlayer(UUID uuid) {
        return new PlayerSpigot(Bukkit.getPlayer(uuid));
    }

    @Override
    public RandomPlayer getPlayer(String name) {
        return new PlayerSpigot(Bukkit.getPlayer(name));
    }
}
