package me.darkeyedragon.randomtp.validator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

public class WorldGuardValidator implements ChunkValidator {
    private final RegionContainer container;
    public WorldGuardValidator() {
        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    @Override
    public boolean isValid(Location location) {

        RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));
        if(regions == null) return true;
        else{
            for (var region : regions.getRegions().values()) {
                if(region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) return false;
            }
        }
        return true;
    }
}
