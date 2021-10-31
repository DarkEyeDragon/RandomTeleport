package me.darkeyedragon.randomtp.addon;

import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SpigotAddonPlugin implements AddonPlugin {

    private final String name;
    private final Plugin plugin;

    public static SpigotAddonPlugin create(String name){
        return new SpigotAddonPlugin(name);
    }


    private SpigotAddonPlugin(String name) {
        this.name = name;
        this.plugin = Bukkit.getPluginManager().getPlugin(name);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getName() {
        return name;
    }

}
