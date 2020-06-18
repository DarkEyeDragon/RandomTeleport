package me.darkeyedragon.randomtp.validator;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import org.bukkit.Location;

public class RedProtectValidator implements ChunkValidator {

    private final RedProtect instance;
    private boolean isLoaded;

    public RedProtectValidator() {
        instance = RedProtect.get();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(Location location) {
        return instance.getAPI().getRegion(location) == null;
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
        return Validator.RED_PROTECT;
    }
}
