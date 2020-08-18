package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.location.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WorldUtil {

    public static World toWorld(RandomWorld world){
        return Bukkit.getWorld(world.getUUID());
    }

    public static RandomWorld toRandomWorld(World world){
        return new RandomWorld() {
            @Override
            public UUID getUUID() {
                return world.getUID();
            }

            @Override
            public RandomBlock getHighestBlockAt(int x, int z) {
                return toRandomBlock(world.getHighestBlockAt(x,z));
            }

            @Override
            public CompletableFuture<RandomChunk> getChunkAtAsync(int x, int z) {
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
                return toRandomWorldBorder(world.getWorldBorder());
            }

            @Override
            public RandomEnvironment getEnvironment() {
                return toRandomEnvironment(world.getEnvironment());
            }
        };
    }
    public static RandomBlock toRandomBlock(Block block){
        return new RandomBlock() {
            @Override
            public RandomLocation getLocation() {
                return LocationUtil.toRandomLocation(block.getLocation());
            }

            @Override
            public boolean isPassable() {
                return block.isPassable();
            }

            @Override
            public boolean isLiquid() {
                return block.isLiquid();
            }

            @Override
            public RandomBlock getRelative(BlockFace blockFace) {
                return toRandomBlock(block.getRelative(toBlockFace(blockFace)));
            }

            @Override
            public boolean isEmpty() {
                return block.isEmpty();
            }
        };
    }
    private static org.bukkit.block.BlockFace toBlockFace(BlockFace blockFace){
        return org.bukkit.block.BlockFace.valueOf(blockFace.name());
    }

    public static RandomWorldBorder toRandomWorldBorder(WorldBorder worldBorder){
        return new RandomWorldBorder() {
            @Override
            public RandomLocation getCenter() {
                return LocationUtil.toRandomLocation(worldBorder.getCenter());
            }

            @Override
            public double getSize() {
                return 0;
            }

            @Override
            public int getWarningDistance() {
                return 0;
            }
        };
    }

    public static RandomEnvironment toRandomEnvironment(World.Environment environment){
        return RandomEnvironment.valueOf(environment.name());
    }
}
