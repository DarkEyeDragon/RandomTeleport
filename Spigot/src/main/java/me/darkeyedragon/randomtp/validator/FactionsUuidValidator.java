package me.darkeyedragon.randomtp.validator;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.location.LocationUtil;
import org.bukkit.Location;

/**
 * Faction validator for https://www.spigotmc.org/resources/factionsuuid.1035
 */
public class FactionsUuidValidator implements PluginLocationValidator {

    private final Board instance;
    private boolean isLoaded;

    public FactionsUuidValidator() {
        instance = Board.getInstance();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = LocationUtil.toLocation(location);
        FLocation fLocation = new FLocation(loc);
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
