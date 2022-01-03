package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.sponge.world.util.WorldUtil;
import org.spongepowered.api.world.World;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpongeWorld implements RandomWorld {

    private final World world;

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
        return null;
    }

    @Override
    public RandomBlock getBlockAt(RandomLocation location) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public RandomBlock getBlockAt(int x, int y, int z) {
        return null;
    }

    @Override
    public RandomWorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public RandomEnvironment getEnvironment() {
        return null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return false;
    }

    @Override
    public void spawnParticle(String particleId, RandomLocation spawnLoc, int amount) {

    }
}
