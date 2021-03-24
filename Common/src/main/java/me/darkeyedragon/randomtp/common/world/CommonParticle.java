package me.darkeyedragon.randomtp.common.world;

import me.darkeyedragon.randomtp.api.world.RandomParticle;

public class CommonParticle implements RandomParticle {

    protected final String id;
    protected final int amount;

    public CommonParticle(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return id + ":" + amount;
    }
}
