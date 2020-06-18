package me.darkeyedragon.randomtp.validator;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

/**
 * Faction validator for https://www.spigotmc.org/resources/factionsuuid.1035
 */
public class FactionsUuidValidator implements ChunkValidator {

    private final Board instance;
    private boolean isLoaded;

    public FactionsUuidValidator() {
        instance = Board.getInstance();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(Location location) {
        FLocation fLocation = new FLocation(location);
        Faction faction = instance.getFactionAt(fLocation);
        return faction.isWilderness() || faction.isWarZone();
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    @Override
    public Validator getValidator() {
        return Validator.FACTIONS;
    }
}
