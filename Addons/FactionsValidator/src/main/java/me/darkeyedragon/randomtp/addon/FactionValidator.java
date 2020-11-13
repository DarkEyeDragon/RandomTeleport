package me.darkeyedragon.randomtp.addon;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.addon.RandomAddon;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Location;

/**
 * Factions Validator
 *
 */
public class FactionValidator extends RandomAddon
{

    @Override
    public String getIdentifier() {
        return "FactionValidator";
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        FLocation fLocation = new FLocation(loc);
        Faction faction = Board.getInstance().getFactionAt(fLocation);
        return faction.isWilderness() || faction.isWarZone();
    }
}
