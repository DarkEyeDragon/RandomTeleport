package me.darkeyedragon.randomtp.api.world;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.UUID;
import java.util.concurrent.Future;

public interface RandomPlayer {

    UUID getUniqueId();

    String getName();

    RandomLocation getLocation();

    RandomWorld getWorld();

    RandomLocation getEyeLocation();

    Future<TeleportResponse> teleportAsync(TeleportProperty teleportProperty);

    RandomCooldown getCooldown();

    void setCooldown(RandomCooldown cooldown);

    boolean hasPermission(String permission);
}
