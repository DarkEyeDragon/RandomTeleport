package me.darkeyedragon.randomtp.sponge.listener;

import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.failsafe.CommonDeathTracker;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;

public class PlayerDeathListener {

    private final RandomTeleportPlugin<?> randomTeleport;

    public PlayerDeathListener(RandomTeleportPlugin<?> randomTeleport) {
        this.randomTeleport = randomTeleport;
    }

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player p) {
        CommonDeathTracker deathTracker = (CommonDeathTracker) randomTeleport.getDeathTracker();
        RandomPlayer player = randomTeleport.getPlayerHandler().getPlayer(p.getUniqueId());
        if (deathTracker.contains(player)) {
            event.setKeepInventory(true);
            deathTracker.remove(player);
            randomTeleport.getMessageHandler().sendMessage(player, "<gold>Whoops. Looks like you died while random teleporting. This is a fail safe and should not occur. Please report this.");
            randomTeleport.getCooldownHandler().removeCooldown(p.getUniqueId());
        }
    }
}
