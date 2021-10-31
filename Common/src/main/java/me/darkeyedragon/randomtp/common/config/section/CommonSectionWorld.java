package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;

import java.util.Set;

public class CommonSectionWorld implements SectionWorld {

    private final Set<ConfigWorld> worlds;

    public CommonSectionWorld(Set<ConfigWorld> worlds) {
        this.worlds = worlds;
    }


    @Override
    public Set<ConfigWorld> getConfigWorlds() {
        return worlds;
    }

    @Override
    public boolean contains(ConfigWorld world) {
        return worlds.contains(world);
    }

    @Override
    public boolean contains(String worldName) {
        return getConfigWorld(worldName) != null;
    }

    @Override
    public boolean remove(ConfigWorld world) {
        return worlds.remove(world);
    }

    @Override
    public boolean remove(String worldName) {
        return worlds.removeIf(configWorld -> configWorld.getName().equals(worldName));
    }

    @Override
    public ConfigWorld getConfigWorld(String worldName) {
        for (ConfigWorld configWorld : worlds) {
            if (configWorld.getName().equals(worldName))
                return configWorld;
        }
        return null;
    }
}
