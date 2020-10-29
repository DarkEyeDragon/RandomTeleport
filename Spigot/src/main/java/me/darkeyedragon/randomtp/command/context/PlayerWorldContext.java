package me.darkeyedragon.randomtp.command.context;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerWorldContext {
    private final List<Player> players;
    private RandomWorld world;

    public PlayerWorldContext() {
        players = new ArrayList<>(1);
    }

    public boolean isWorld() {
        return world != null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public RandomWorld getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = WorldUtil.toRandomWorld(world);
    }

    public void setWorld(RandomWorld world) {
        this.world = world;
    }
}
