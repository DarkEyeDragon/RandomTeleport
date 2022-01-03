package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.failsafe.CommonDeathTracker;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final RandomTeleport randomTeleport;

    public PlayerDeathListener(RandomTeleport randomTeleport) {
        this.randomTeleport = randomTeleport;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        CommonDeathTracker deathTracker = (CommonDeathTracker) randomTeleport.getDeathTracker();
        RandomPlayer player = randomTeleport.getPlayerHandler().getPlayer(event.getEntity().getUniqueId());
        if (deathTracker.contains(player)) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.getDrops().clear();
            deathTracker.remove(player);
            event.getEntity().sendMessage(ChatColor.GOLD + "Whoops. Looks like you died while random teleporting. This is a fail safe and should not occur. Please report this.");
            randomTeleport.getCooldownHandler().removeCooldown(event.getEntity().getUniqueId());
        }
    }
}
