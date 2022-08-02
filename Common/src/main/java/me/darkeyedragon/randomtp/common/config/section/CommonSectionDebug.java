package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionDebug;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionDebug implements SectionDebug {

    boolean showQueuePopulation;
    boolean showExecutionTimes;
    boolean showSearchingMessages;

    @Override
    public boolean isShowQueuePopulation() {
        return showQueuePopulation;
    }

    @Override
    public boolean isShowSearchingMessages() {
        return showSearchingMessages;
    }

    @Override
    public boolean isShowExecutionTimes() {
        return showExecutionTimes;
    }
}
