package me.darkeyedragon.randomtp.config.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConfigPlugin {

    private final Set<String> plugins;

    public ConfigPlugin() {
        this.plugins = new HashSet<>();
    }

    public void add(String plugin){
        plugins.add(plugin);
    }
    public ConfigPlugin addAll(Collection<String> collection){
        plugins.addAll(collection);
        return this;
    }
    public Set<String> getPlugins() {
        return plugins;
    }
}
