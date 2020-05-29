package me.darkeyedragon.randomtp.config.data;

public class ConfigTeleport {


    private long cooldown;
    private long delay;
    private boolean cancelOnMove;

    public ConfigTeleport cooldown(long cooldown){
        this.cooldown = cooldown;
        return this;
    }

    public ConfigTeleport delay(int delay){
        this.delay = delay;
        return this;
    }

    public ConfigTeleport cancelOnMove(boolean cancelOnMove){
        this.cancelOnMove = cancelOnMove;
        return this;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isCancelOnMove() {
        return cancelOnMove;
    }
}
