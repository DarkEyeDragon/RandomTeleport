package me.darkeyedragon.randomtp.api.addon;


import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;

import java.util.Map;

public interface RandomAddonManager {
    RandomAddon unregister(String name);

    RandomAddon register(String name);

    Map<String, RandomAddon> getAddons();

    RandomTeleportPlugin<?> getInstance();
}
