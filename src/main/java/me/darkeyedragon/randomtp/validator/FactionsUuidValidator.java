package me.darkeyedragon.randomtp.validator;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

/**
 * Faction validator for https://www.spigotmc.org/resources/factionsuuid.1035
 */
public class FactionsUuidValidator implements ChunkValidator {

    @Override
    public boolean isValid(Location location) {
        FLocation fLocation = new FLocation(location);
        Faction faction = Board.getInstance().getFactionAt(fLocation);
        return faction.isWilderness() || faction.isWarZone();
    }
}
