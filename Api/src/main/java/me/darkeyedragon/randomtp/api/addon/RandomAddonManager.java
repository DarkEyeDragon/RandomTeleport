package me.darkeyedragon.randomtp.api.addon;


public interface RandomAddonManager {
    RandomLocationValidator unregister(String name);

    RandomLocationValidator register(String name);

}
