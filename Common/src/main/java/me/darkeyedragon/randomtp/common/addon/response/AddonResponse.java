package me.darkeyedragon.randomtp.common.addon.response;

import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;
import me.darkeyedragon.randomtp.common.addon.RandomAddon;

import java.util.HashSet;
import java.util.Set;

public class AddonResponse {

    private AddonResponseType responseType;
    final private RandomAddon addon;
    //Boolean will be true if the plugin is loaded
    final private Set<RequiredPlugin> missingPlugins;
    private String minVersion;
    private String maxVersion;

    public AddonResponse(RandomAddon addon) {
        this.addon = addon;
        this.missingPlugins = new HashSet<>();
    }

    public boolean addMissingPlugin(RequiredPlugin key) {
        return missingPlugins.add(key);
    }

    public AddonResponseType getResponseType() {
        return responseType;
    }

    public RandomAddon getAddon() {
        return addon;
    }

    public Set<RequiredPlugin> getMissingPlugins() {
        return missingPlugins;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setResponseType(AddonResponseType responseType) {
        this.responseType = responseType;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }
}
