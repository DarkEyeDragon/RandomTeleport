package me.darkeyedragon.randomtp.world;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.darkeyedragon.randomtp.world.block.SpigotBlock;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpigotWorld implements RandomWorld {

    private final World world;

    public SpigotWorld(World world) {
        this.world = world;
    }

    @Override
    public UUID getUUID() {
        return world.getUID();
    }

    @Override
    public RandomBlock getHighestBlockAt(int x, int z) {
        return new SpigotBlock(world.getHighestBlockAt(x, z));
    }

    @Override
    public CompletableFuture<RandomChunkSnapshot> getChunkAtAsync(RandomWorld world, int x, int z) {
        World regWorld = WorldUtil.toWorld(world);
        if (PaperLib.isPaper()) {
            return regWorld.getChunkAtAsync(x, z, true, true).thenApply(chunk -> new SpigotChunkSnapshot(chunk.getChunkSnapshot(true, true, false)));
        }
        return PaperLib.getChunkAtAsync(regWorld, x, z, true, true).thenApply(chunk -> new SpigotChunkSnapshot(chunk.getChunkSnapshot(true, true, false)));
    }

    @Override
    public RandomBlock getBlockAt(RandomLocation location) {
        return new SpigotBlock(world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public RandomBlock getBlockAt(int x, int y, int z) {
        return new SpigotBlock(world.getBlockAt(x, y, z));
    }

    @Override
    public RandomWorldBorder getWorldBorder() {
        return new SpigotWorldBorder(world.getWorldBorder());
    }

    @Override
    public RandomEnvironment getEnvironment() {
        return WorldUtil.toRandomEnvironment(world.getEnvironment());
    }

    @Override
    public int hashCode() {
        return world.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (world == obj) return true;
        if (obj instanceof RandomWorld) {
            return this.getUUID().equals(((RandomWorld) obj).getUUID());
        }
        return false;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return world.isChunkLoaded(x, z);
    }

    @Override
    public void spawnParticle(String particleId, RandomLocation spawnLoc, int amount) {
        world.spawnParticle(Particle.valueOf(particleId), WorldUtil.toLocation(spawnLoc), amount);
    }
}
