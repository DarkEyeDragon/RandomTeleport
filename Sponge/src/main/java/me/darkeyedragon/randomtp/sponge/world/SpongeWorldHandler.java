package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.RandomBiomeHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.common.world.WorldHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SpongeWorldHandler extends WorldHandler {

    private final RandomBiomeHandler biomeHandler;

    public SpongeWorldHandler(RandomTeleportPlugin<?> plugin, RandomBiomeHandler biomeHandler) {
        super(plugin);
        this.biomeHandler = biomeHandler;
    }

    @Override
    public RandomBiomeHandler getBiomeHandler() {
        return biomeHandler;
    }

    @Override
    public RandomWorld getWorld(String worldName) {
        Optional<World> optionalWorld = Sponge.getServer().getWorld(worldName);
        return optionalWorld.map(SpongeWorld::new).orElse(null);
    }
}
