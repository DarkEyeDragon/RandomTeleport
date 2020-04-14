package me.darkeyedragon.randomtp.command.context;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerWorldContext {
    private Player player;
    private World world;

    public boolean isPlayer() {
        return player != null;
    }

    public boolean isWorld() {
        return world != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
