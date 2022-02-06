package me.darkeyedragon.randomtp.sponge.addon;

import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

public class SpongeAddonPlugin implements AddonPlugin {

    private final String name;
    private final PluginContainer plugin;

    private SpongeAddonPlugin(String name) {
        this.name = name;
        this.plugin = Sponge.getPluginManager().getPlugin(name).orElseThrow(() -> new IllegalStateException("Plugin " + name + " does not exist!"));
    }

    public static SpongeAddonPlugin create(String name) {
        return new SpongeAddonPlugin(name);
    }

    @Override
    public String getVersion() {
        return plugin.getVersion().orElseThrow(() -> new IllegalStateException("Version of " + name + " is null!"));
    }

    @Override
    public String getName() {
        return name;
    }

}