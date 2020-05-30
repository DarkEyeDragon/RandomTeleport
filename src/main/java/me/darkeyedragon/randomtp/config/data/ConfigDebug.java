package me.darkeyedragon.randomtp.config.data;

public class ConfigDebug {

    private boolean showQueuePopulation;

    public ConfigDebug showQueuePopulation(boolean showQueuePopulation){
        this.showQueuePopulation = showQueuePopulation;
        return this;
    }

    public boolean isShowQueuePopulation() {
        return showQueuePopulation;
    }
}
