package me.darkeyedragon.randomtp.api.teleport;

import me.darkeyedragon.randomtp.api.world.RandomWorld;

public class TeleportProperty {

    private final RandomWorld world;
    private final boolean bypassCooldown;
    private final boolean ignoreTeleportDelay;
    private final boolean useEco;
    private final long cooldown;

    public TeleportProperty(RandomWorld world, boolean bypassCooldown, boolean ignoreTeleportDelay, boolean useEco, long cooldown) {
        this.world = world;
        this.bypassCooldown = bypassCooldown;
        this.ignoreTeleportDelay = ignoreTeleportDelay;
        this.useEco = useEco;
        this.cooldown = cooldown;
    }

    public RandomWorld getWorld() {
        return world;
    }

    public boolean isBypassCooldown() {
        return bypassCooldown;
    }

    public boolean isIgnoreTeleportDelay() {
        return ignoreTeleportDelay;
    }

    public boolean isUseEco() {
        return useEco;
    }

    public long getCooldown() {
        return cooldown;
    }
}
