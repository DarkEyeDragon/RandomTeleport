package me.darkeyedragon.randomtp.common.addon;

import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomAddon implements RandomLocationValidator {

    private List<RequiredPlugin> requiredPlugins;

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
}
