package me.darkeyedragon.randomtp.validator;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.Location;

public class TownyValidator implements ChunkValidator {

    private final TownyAPI instance;
    private boolean isLoaded;

    public TownyValidator() {
        this.instance = TownyAPI.getInstance();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(Location location) {
        return instance.isWilderness(location);
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
