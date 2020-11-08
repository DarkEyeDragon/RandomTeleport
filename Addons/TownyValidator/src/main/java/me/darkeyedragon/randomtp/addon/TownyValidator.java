package me.darkeyedragon.randomtp.addon;

import com.palmergames.bukkit.towny.TownyAPI;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.addon.RandomAddon;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Location;

/**
 * Towny location validator
 */
public class TownyValidator extends RandomAddon {

    private final TownyAPI instance;

    public TownyValidator() {
        this.instance = TownyAPI.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "Towny";
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        return instance.isWilderness(loc);
    }
}
