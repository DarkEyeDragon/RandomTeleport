package me.darkeyedragon.randomtp.api.addon;


import java.util.ArrayList;
import java.util.List;

public abstract class RandomAddon implements RandomLocationValidator {

    private List<RequiredPlugin> requiredPlugins;
    private RandomAddonManager addonManager;
    public RandomAddon() {
        requiredPlugins = new ArrayList<>();
    }

    public RandomAddon(List<RequiredPlugin> requiredPlugins){
        this.requiredPlugins = requiredPlugins;
    }

    public List<RequiredPlugin> getRequiredPlugins() {
        return requiredPlugins;
    }

    public void addRequiredPlugin(RequiredPlugin requiredPlugin) {
        this.requiredPlugins.add(requiredPlugin);
    }

    public void setRequiredPlugins(List<RequiredPlugin> requiredPlugins) {
        this.requiredPlugins = requiredPlugins;
    }

    public RandomAddonManager getAddonManager() {
        return addonManager;
    }

    public void setAddonManager(RandomAddonManager addonManager) {
        this.addonManager = addonManager;
    }
}
