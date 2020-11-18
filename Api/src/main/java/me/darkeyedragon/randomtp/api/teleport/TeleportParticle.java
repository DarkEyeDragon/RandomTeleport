package me.darkeyedragon.randomtp.api.teleport;

import org.jetbrains.annotations.Nullable;

public class TeleportParticle<T> implements RandomParticle<T> {


    private final T particle;
    private final int amount;

    /**
     * @param particle the {@link RandomParticle} you want to show.
     *                 null if no particle should be displayed.
     */
    public TeleportParticle(@Nullable T particle, int amount) {
        this.particle = particle;
        this.amount = amount;
    }



    public int getAmount() {
        return amount;
    }

    @Override
    public T getParticle() {
        return particle;
    }
}
