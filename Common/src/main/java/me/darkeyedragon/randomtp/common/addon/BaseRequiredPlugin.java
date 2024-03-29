package me.darkeyedragon.randomtp.common.addon;

import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;

public class BaseRequiredPlugin implements RequiredPlugin {

    private final String minVersion;
    private final String name;
    private final String maxVersion;
    private boolean loaded;

    public BaseRequiredPlugin(String name, String minVersion, String maxVersion) {
        this.minVersion = minVersion;
        this.name = name;
        this.maxVersion = maxVersion;
    }

    public BaseRequiredPlugin(String name, String minVersion) {
        this.minVersion = minVersion;
        this.maxVersion = null;
        this.name = name;
    }

    public BaseRequiredPlugin(String name) {
        this.minVersion = null;
        this.maxVersion = null;
        this.name = name;
    }

    @Override
    public String getMinVersion() {
        return minVersion;
    }

    @Override
    public String getMaxVersion() {
        return maxVersion;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getName() {
        return name;
    }

}
