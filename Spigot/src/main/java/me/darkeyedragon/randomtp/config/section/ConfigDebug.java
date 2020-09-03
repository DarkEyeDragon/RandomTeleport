package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionDebug;

public class ConfigDebug implements SectionDebug {

    private boolean showQueuePopulation;

    public ConfigDebug showQueuePopulation(boolean showQueuePopulation){
        this.showQueuePopulation = showQueuePopulation;
        return this;
    }

    public boolean isShowQueuePopulation() {
        return showQueuePopulation;
    }
}
