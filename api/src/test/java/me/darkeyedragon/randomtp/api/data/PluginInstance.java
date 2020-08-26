package me.darkeyedragon.randomtp.api.data;

import me.darkeyedragon.randomtp.api.RandomPlugin;

import java.util.HashSet;

public interface PluginInstance {
    RandomPlugin instance = HashSet::new;
}
