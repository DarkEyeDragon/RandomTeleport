package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.WorldHandler;
import org.bukkit.Bukkit;

public class SpigotWorldHandler extends WorldHandler {

    public SpigotWorldHandler(RandomTeleportPlugin<?> plugin) {
        super(plugin);
    }

    @Override
    public RandomWorld getWorld(String worldName) {
        return new SpigotWorld(Bukkit.getWorld(worldName));
    }
}
