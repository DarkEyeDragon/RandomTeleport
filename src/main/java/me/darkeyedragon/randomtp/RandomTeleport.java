package me.darkeyedragon.randomtp;

import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.command.Teleport;
import me.darkeyedragon.randomtp.util.LocationHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class RandomTeleport extends JavaPlugin {

    private HashMap<UUID, Long> cooldowns;
    private PaperCommandManager manager;
    @Override
    public void onEnable() {
        // Plugin startup logicµ
        manager = new PaperCommandManager(this);
        cooldowns = new HashMap<>();
        saveDefaultConfig();
        manager.registerCommand(new Teleport(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getLogger().info("Good night dad!");
    }

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }
}
