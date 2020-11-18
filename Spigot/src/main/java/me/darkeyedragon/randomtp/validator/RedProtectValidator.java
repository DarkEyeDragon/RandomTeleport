package me.darkeyedragon.randomtp.validator;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Location;

public class RedProtectValidator implements PluginLocationValidator {

    private final String name;
    private RedProtect instance;
    private boolean isLoaded;

    public RedProtectValidator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        return instance.getAPI().getRegion(loc) == null;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void load() {
        instance = RedProtect.get();
        this.isLoaded = instance != null;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

}
