package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConfigPlugin implements SectionPlugin {

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
