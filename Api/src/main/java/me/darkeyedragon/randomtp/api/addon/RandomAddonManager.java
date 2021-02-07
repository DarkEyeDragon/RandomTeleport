package me.darkeyedragon.randomtp.api.addon;


import java.util.Map;

public interface RandomAddonManager {
    RandomAddon unregister(String name);

    RandomAddon register(String name);

    Map<String, RandomAddon> getAddons();
}
