package me.darkeyedragon.randomtp.sponge.world.util;

import com.flowpowered.math.vector.Vector3d;
import me.darkeyedragon.randomtp.api.world.RandomBiome;
import me.darkeyedragon.randomtp.api.world.RandomChunkSnapshot;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldBorder;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.world.location.CommonLocation;
import me.darkeyedragon.randomtp.sponge.world.SpongeBiome;
import me.darkeyedragon.randomtp.sponge.world.SpongeChunk;
import me.darkeyedragon.randomtp.sponge.world.SpongeWorld;
import me.darkeyedragon.randomtp.sponge.world.SpongeWorldBorder;
import me.darkeyedragon.randomtp.sponge.world.block.SpongeBlock;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

public class WorldUtil {

    public static World toWorld(RandomWorld world) {
        return Sponge.getServer().getWorld(world.getUUID()).get();
    }

    public static RandomWorld toRandomWorld(World world) {
        return new SpongeWorld(world);
    }

    public static RandomBlock toRandomBlock(Location<World> location) {
        return new SpongeBlock(location);
    }

    /*private static org.bukkit.block.BlockFace toBlockFace(BlockFace blockFace) {
        return org.bukkit.block.BlockFace.valueOf(blockFace.name());
    }*/

    public static RandomWorldBorder toRandomWorldBorder(World world) {
        return new SpongeWorldBorder(world);
    }

    public static RandomChunkSnapshot toRandomChunk(Location<Chunk> location) {
        return new SpongeChunk(location);
    }

    /*public static RandomEnvironment toRandomEnvironment(World.Environment environment) {
        return RandomEnvironment.valueOf(environment.name());
    }*/

    public static RandomBiome toRandomBiome(BiomeType biome) {
        return new SpongeBiome(biome);
    }

    public static RandomLocation toRandomLocation(Location<World> location) {
        return new CommonLocation(WorldUtil.toRandomWorld(location.getExtent()), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static RandomLocation toRandomLocation(World world, Vector3d vector3d) {
        return new CommonLocation(WorldUtil.toRandomWorld(world), vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }

    public static Location<World> toLocation(RandomLocation location) {
        return new Location<>(WorldUtil.toWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

    public static Vector3d toVector3d(RandomLocation location) {
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }
}
