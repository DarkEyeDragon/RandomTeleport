package me.darkeyedragon.randomtp.addon;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.addon.RandomAddon;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;

/**
 * Griefprevention location validator
 *
 */
public class GriefPreventionValidator extends RandomAddon
{
    @Override
    public String getIdentifier() {
        return "GriefPrevention";
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);
        return claim == null;
    }
}
