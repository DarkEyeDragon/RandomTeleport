package me.darkeyedragon.randomtp.util;

import me.darkeyedragon.randomtp.api.world.RandomChunk;
import me.darkeyedragon.randomtp.api.world.RandomEnvironment;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.BlockFace;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.world.SpigotChunk;
import me.darkeyedragon.randomtp.world.SpigotWorld;
import me.darkeyedragon.randomtp.world.SpigotWorldBorder;
import me.darkeyedragon.randomtp.world.block.SpigotBlock;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class WorldUtil {

    public static final Map<World, RandomWorld> WORLd_MAP = new HashMap<>();

    public static World toWorld(RandomWorld world) {
        return Bukkit.getWorld(world.getUUID());
    }

    public static RandomWorld toRandomWorld(World world) {
        if (WORLd_MAP.containsKey(world)) {
            return WORLd_MAP.get(world);
        }
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

    public static RandomChunk toRandomChunk(Chunk chunk) {
        return new SpigotChunk(chunk);
    }

    public static RandomEnvironment toRandomEnvironment(World.Environment environment) {
        return RandomEnvironment.valueOf(environment.name());
    }
}
