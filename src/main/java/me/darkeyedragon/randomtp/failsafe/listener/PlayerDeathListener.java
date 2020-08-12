package me.darkeyedragon.randomtp.failsafe.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
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
        DeathTracker deathTracker = randomTeleport.getDeathTracker();
        if (deathTracker.contains(event.getEntity())) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.getDrops().clear();
            deathTracker.remove(event.getEntity());
            event.getEntity().sendMessage(ChatColor.GOLD + "Whoops. Looks like you died while random teleporting. This is a fail safe and should not occur. Please report this.");
            randomTeleport.getCooldowns().remove(event.getEntity().getUniqueId());
        }
    }

}
