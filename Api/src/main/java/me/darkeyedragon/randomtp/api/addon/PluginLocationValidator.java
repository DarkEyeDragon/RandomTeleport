package me.darkeyedragon.randomtp.api.addon;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface PluginLocationValidator {

    String getName();

    boolean isValid(RandomLocation location);

    boolean isLoaded();

    void load();

    void setLoaded(boolean loaded);
}
