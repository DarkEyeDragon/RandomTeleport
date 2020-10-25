package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionDebug;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigDebug implements SectionDebug {

    @Setting(value = "show_queue_population")
    protected boolean showQueuePopulation;

    @Override
    public boolean isShowQueuePopulation() {
        return showQueuePopulation;
    }
}
