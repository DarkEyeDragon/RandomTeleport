package me.darkeyedragon.randomtp.api.addon;


import me.darkeyedragon.randomtp.api.plugin.Platform;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;

import java.util.Map;

public interface RandomAddonManager {
    Platform getPlatform();

    RandomAddon unregister(String name);

    RandomAddon register(String name) throws ReflectiveOperationException;

    Map<String, RandomAddon> getAddons();

    RandomTeleportPlugin<?> getInstance();
}
