package me.darkeyedragon.randomtp.api;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;
import me.darkeyedragon.randomtp.api.config.Blacklist;

import java.util.Set;

public interface RandomPlugin<T> {

    Set<PluginLocationValidator> getValidatorList();

    Blacklist<T> getBlacklist();
}
