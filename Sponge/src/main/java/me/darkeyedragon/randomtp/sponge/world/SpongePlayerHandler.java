package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import org.spongepowered.api.Sponge;

import java.util.UUID;

public class SpongePlayerHandler implements PlayerHandler {
    @Override
    public RandomPlayer getPlayer(UUID uuid) {
        return new SpongePlayer(Sponge.getServer().getPlayer(uuid).orElse(null));
    }

    @Override
    public RandomPlayer getPlayer(String name) {
        return null;
    }
}
