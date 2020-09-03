package me.darkeyedragon.randomtp.api.addon;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface PluginLocationValidator {
    boolean isValid(RandomLocation location);

    boolean isLoaded();

    void setLoaded(boolean loaded);

    Validator getValidator();
}
