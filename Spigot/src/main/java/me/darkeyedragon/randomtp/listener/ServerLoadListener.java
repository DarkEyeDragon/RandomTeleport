package me.darkeyedragon.randomtp.listener;

import me.darkeyedragon.randomtp.RandomTeleport;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener {

    private final RandomTeleport instance;


    public ServerLoadListener(RandomTeleport instance) {
        this.instance = instance;
    }

    /**
     * Listens to the server load event to then load the addons to prevent plugins not being loaded yet.
     */
    @EventHandler
    public void onServerLoad(ServerLoadEvent event){
        instance.getLogger().info(ChatColor.AQUA + "======== [Loading validators] ========");
        instance.getAddonManager().instantiateAllLocal();
        instance.getLogger().info(ChatColor.AQUA + "======================================");
        instance.populateWorldQueue();
    }

}
