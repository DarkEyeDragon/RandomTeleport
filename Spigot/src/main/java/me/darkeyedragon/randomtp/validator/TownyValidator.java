package me.darkeyedragon.randomtp.validator;

import com.palmergames.bukkit.towny.TownyAPI;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Location;

public class TownyValidator implements RandomLocationValidator {

    private final String name;
    private TownyAPI instance;
    private boolean isLoaded;

    public TownyValidator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        return instance.isWilderness(loc);
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void load() {
        this.instance = TownyAPI.getInstance();
        setLoaded(instance != null);
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

}
