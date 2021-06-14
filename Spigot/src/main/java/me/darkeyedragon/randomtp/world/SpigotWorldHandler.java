package me.darkeyedragon.randomtp.world;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SpigotWorldHandler extends WorldHandler {

    private final RandomBiomeHandler biomeHandler;

    public SpigotWorldHandler(RandomTeleportPlugin<?> plugin, RandomBiomeHandler biomeHandler) {
        super(plugin);
        this.biomeHandler = biomeHandler;
    }

    @Override
    public RandomWorld getWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return new SpigotWorld(world);
        }
        return null;
    }

    public RandomBiomeHandler getBiomeHandler() {
        return biomeHandler;
    }
}
