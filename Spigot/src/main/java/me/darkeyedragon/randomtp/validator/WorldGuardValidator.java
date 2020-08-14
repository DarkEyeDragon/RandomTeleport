package me.darkeyedragon.randomtp.validator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

public class WorldGuardValidator implements ChunkValidator {
    private final WorldGuard instance;
    private boolean isLoaded;

    public WorldGuardValidator() {
        instance = WorldGuard.getInstance();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(Location location) {

        RegionManager regions = instance.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (regions == null) return true;
        else {
            for (ProtectedRegion region : regions.getRegions().values()) {
                if (region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @Override
    public Validator getValidator() {
        return Validator.WORLD_GUARD;
    }
}
