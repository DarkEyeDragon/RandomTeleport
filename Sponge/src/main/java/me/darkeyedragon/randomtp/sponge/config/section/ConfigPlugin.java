package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionPlugin;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ConfigSerializable
public class ConfigPlugin implements SectionPlugin {

    @Setting
    private List<String> plugins;

    @Override
    public Set<String> getPlugins() {
        return new HashSet<>(plugins);
    }
}
