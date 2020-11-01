package me.darkeyedragon.randomtp.validator;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class GriefPreventionValidator implements PluginLocationValidator {

    private final String name;
    private Plugin instance;
    private boolean isLoaded;

    public GriefPreventionValidator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);
        return claim == null;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void load() {
        instance = GriefPrevention.instance;
        setLoaded(instance != null);
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

}
