package me.darkeyedragon.randomtp;

import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.command.Teleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.validator.ChunkValidator;
import me.darkeyedragon.randomtp.validator.ValidatorFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class RandomTeleport extends JavaPlugin {

    private HashMap<UUID, Long> cooldowns;
    private PaperCommandManager manager;
    private List<ChunkValidator> validatorList;
    private ConfigHandler configHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        manager = new PaperCommandManager(this);
        configHandler = new ConfigHandler(this);
        cooldowns = new HashMap<>();
        saveDefaultConfig();
        manager.registerCommand(new Teleport(this));
        validatorList = new ArrayList<>();
        configHandler.getPlugins().forEach(s -> {
            if (getServer().getPluginManager().getPlugin(s) != null) {
                var validator = ValidatorFactory.createFrom(s);
                if (validator != null) {
                    validatorList.add(validator);
                    getLogger().info(s + " loaded as validator.");
                }
            } else {
                getLogger().warning(s + " is not a valid plugin or is not loaded!");
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getLogger().info("Good night dad!");
    }

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    public List<ChunkValidator> getValidatorList() {
        return validatorList;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
