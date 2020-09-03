package me.darkeyedragon.randomtp.api;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;

import java.util.Set;

public interface RandomPlugin {

    Set<PluginLocationValidator> getValidatorList();
}
