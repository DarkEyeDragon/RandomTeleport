package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionQueue implements SectionQueue {

    private int size;
    private long initDelay;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public long getInitDelay() {
        return initDelay;
    }
}
