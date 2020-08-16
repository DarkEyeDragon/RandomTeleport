package me.darkeyedragon.randomtp.validator;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.location.LocationUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class GriefPreventionValidator implements PluginLocationValidator {

    private final Plugin instance;
    private boolean isLoaded;

    public GriefPreventionValidator() {
        instance = GriefPrevention.instance;
        isLoaded = instance != null;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = LocationUtil.toLocation(location);
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);
        return claim == null;
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
        return Validator.GRIEF_PREVENTION;
    }
}
