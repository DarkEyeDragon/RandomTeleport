package me.darkeyedragon.randomtp.config.data;

public class ConfigQueue {

    private int size;
    private int initDelay;

    public ConfigQueue size(int size){
        this.size = size;
        return this;
    }

    public ConfigQueue initDelay(int initDelay){
        this.initDelay = initDelay;
        return this;
    }

    public int getSize() {
        return size;
    }

    public int getInitDelay() {
        return initDelay;
    }
}
