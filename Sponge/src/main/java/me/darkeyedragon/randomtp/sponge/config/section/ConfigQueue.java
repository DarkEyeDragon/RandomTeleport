package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigQueue implements SectionQueue {

    @Setting
    private int size;

    @Setting
    private long init_delay;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public long getInitDelay() {
        return init_delay;
    }
}
