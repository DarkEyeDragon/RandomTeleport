package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.sponge.world.util.WorldUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpongeWorld implements RandomWorld {

    private final World world;
    private static Collection<ParticleType> particleTypes;
    public SpongeWorld(World world) {
        this.world = world;
    }

    @Override
    public UUID getUUID() {
        return world.getUniqueId();
    }

    @Override
    public RandomBlock getHighestBlockAt(int x, int z) {
        int y = world.getHighestYAt(x, z);
        return WorldUtil.toRandomBlock(world.getLocation(x, y, z));
    }

    @Override
    public CompletableFuture<RandomChunkSnapshot> getChunkAtAsync(RandomWorld world, int x, int z) {
        return this.world.loadChunkAsync(x, 0, z, true).thenApply(chunk -> {
            final Chunk finalChunk = chunk.get();
            return new SpongeChunk(new Location<>(finalChunk, x, 0, z));
        });
    }

    @Override
    public RandomBlock getBlockAt(RandomLocation location) {
        return WorldUtil.toRandomBlock(WorldUtil.toLocation(location));
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public RandomBlock getBlockAt(int x, int y, int z) {
        return WorldUtil.toRandomBlock(new Location<>(world, x, y, z));
    }

    @Override
    public RandomWorldBorder getWorldBorder() {
        return WorldUtil.toRandomWorldBorder(world);
    }

    @Override
    public RandomEnvironment getEnvironment() {
        DimensionType dimensionType = world.getDimension().getType();
        try {
            return RandomEnvironment.valueOf(dimensionType.toString().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            //Ignored, silently fail and let the LocationHandlerFactory deal with it
        }
        return null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        Optional<Chunk> chunk = world.getChunk(x, 0, z);
        return chunk.map(Extent::isLoaded).orElse(false);
    }

    @Override
    public void spawnParticle(String particleId, RandomLocation spawnLoc, int amount) {
        ParticleEffect.Builder builder = ParticleEffect.builder();
        for (ParticleType particleType : getParticleTypes()) {
            if (particleType.getName().equalsIgnoreCase(particleId)) {
                builder.type(particleType);
                break;
            }
        }
        builder.quantity(amount);
        world.spawnParticles(builder.build(), WorldUtil.toVector3d(spawnLoc));
    }

    private Collection<ParticleType> getParticleTypes() {
        if (particleTypes == null) {
            particleTypes = Sponge.getRegistry().getAllOf(ParticleType.class);
        }
        return particleTypes;
    }

    @Override
    public String toString() {
        return "SpongeWorld{" +
                "world=" + world.getName() +
                '}';
    }
}
