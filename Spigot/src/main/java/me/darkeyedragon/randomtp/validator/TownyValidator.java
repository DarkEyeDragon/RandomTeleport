package me.darkeyedragon.randomtp.validator;

import com.palmergames.bukkit.towny.TownyAPI;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.location.LocationUtil;
import org.bukkit.Location;

public class TownyValidator implements PluginLocationValidator {

    private final TownyAPI instance;
    private boolean isLoaded;

    public TownyValidator() {
        this.instance = TownyAPI.getInstance();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = LocationUtil.toLocation(location);
        return instance.isWilderness(loc);
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
        return Validator.TOWNY;
    }
}
