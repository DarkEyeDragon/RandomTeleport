package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.sponge.world.util.WorldUtil;
import org.spongepowered.api.data.property.entity.EyeLocationProperty;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

public class SpongePlayer implements RandomPlayer {

    private final Player player;
    private RandomCooldown cooldown;

    public SpongePlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public RandomLocation getLocation() {
        return WorldUtil.toRandomLocation(player.getLocation());
    }

    @Override
    public RandomWorld getWorld() {
        return WorldUtil.toRandomWorld(player.getWorld());
    }

    @Override
    public RandomLocation getEyeLocation() {
        Optional<EyeLocationProperty> eyeLocationProperty = player.getProperty(EyeLocationProperty.class);
        if (eyeLocationProperty.isPresent()) {
            EyeLocationProperty property = eyeLocationProperty.get();
            if (property.getValue() != null) {
                return WorldUtil.toRandomLocation(player.getWorld(), property.getValue());
            }
        }
        throw new IllegalArgumentException("EyeLocationProperty cannot be null");
    }

    @Override
    public Future<TeleportResponse> teleportAsync(RandomLocation location) {
        return null;
    }

    @Override
    public RandomCooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(RandomCooldown cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void teleport(RandomLocation location) {
        player.setLocation(WorldUtil.toLocation(location));
    }
}
