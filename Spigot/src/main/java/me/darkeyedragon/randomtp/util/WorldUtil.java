package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;
import me.darkeyedragon.randomtp.world.SpigotBiome;
import me.darkeyedragon.randomtp.world.SpigotChunkSnapshot;
import me.darkeyedragon.randomtp.world.SpigotWorld;
import me.darkeyedragon.randomtp.world.SpigotWorldBorder;
import me.darkeyedragon.randomtp.world.block.SpigotBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class WorldUtil {


    public static World toWorld(RandomWorld world) {
        return Bukkit.getWorld(world.getUUID());
    }

    public static RandomWorld toRandomWorld(World world) {
        return new SpigotWorld(world);
    }

    public static RandomBlock toRandomBlock(Block block) {
        return new SpigotBlock(block);
    }

    private static org.bukkit.block.BlockFace toBlockFace(BlockFace blockFace) {
        return org.bukkit.block.BlockFace.valueOf(blockFace.name());
    }

    public static RandomWorldBorder toRandomWorldBorder(WorldBorder worldBorder) {
        return new SpigotWorldBorder(worldBorder);
    }

    public static RandomChunkSnapshot toRandomChunk(ChunkSnapshot chunk) {
        return new SpigotChunkSnapshot(chunk);
    }

    public static RandomEnvironment toRandomEnvironment(World.Environment environment) {
        if (environment == World.Environment.NORMAL) {
            return RandomEnvironment.OVERWORLD;
        }
        return RandomEnvironment.valueOf(environment.name());
    }

    public static RandomBiome toRandomBiome(Biome biome){
        return new SpigotBiome(biome);
    }

    public static RandomLocation toRandomLocation(Location location){
        return new CommonLocation(WorldUtil.toRandomWorld(location.getWorld()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Location toLocation(RandomLocation location){
        return new Location(WorldUtil.toWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }
}
