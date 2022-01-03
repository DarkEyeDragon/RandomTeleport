package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.WorldHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SpongeWorldHandler extends WorldHandler {

    public SpongeWorldHandler(RandomTeleportPlugin<?> plugin) {
        super(plugin);
    }

    @Override
    public RandomBiomeHandler getBiomeHandler() {
        return null;
    }

    @Override
    public RandomWorld getWorld(String worldName) {
        Optional<World> optionalWorld = Sponge.getServer().getWorld(worldName);
        return optionalWorld.map(SpongeWorld::new).orElse(null);
    }
}
