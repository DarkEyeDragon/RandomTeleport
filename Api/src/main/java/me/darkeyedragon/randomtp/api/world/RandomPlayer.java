package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.UUID;
import java.util.function.Consumer;

public interface RandomPlayer {

    UUID getUniqueId();

    RandomLocation getLocation();

    RandomWorld getWorld();

    RandomLocation getEyeLocation();

    Consumer<TeleportResponse> teleportAsync(RandomLocation randomLocation);

    RandomCooldown getCooldown();

    void setCooldown(RandomCooldown cooldown);

    boolean hasPermission(String permission);
}
