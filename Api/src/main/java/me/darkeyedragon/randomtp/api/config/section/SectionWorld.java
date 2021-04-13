package me.darkeyedragon.randomtp.api.config.section;

import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;

import java.util.Set;

public interface SectionWorld {

    Set<ConfigWorld> getConfigWorlds();

    boolean contains(ConfigWorld world);

    boolean contains(String worldName);

    boolean remove(ConfigWorld world);

    boolean remove(String worldName);

    ConfigWorld getConfigWorld(String worldName);
}
