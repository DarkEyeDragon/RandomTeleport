package me.darkeyedragon.randomtp.command.context;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerWorldContext {
    private boolean isPlayer = false;
    private boolean isWorld = false;
    private Player player;
    private World world;

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public boolean isWorld() {
        return isWorld;
    }

    public void setWorld(boolean world) {
        isWorld = world;
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
