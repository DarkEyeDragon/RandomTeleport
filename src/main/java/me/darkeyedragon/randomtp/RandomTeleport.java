package me.darkeyedragon.randomtp;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import me.darkeyedragon.randomtp.command.TeleportCommand;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.validator.ChunkValidator;
import me.darkeyedragon.randomtp.validator.ValidatorFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
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

        //check if the first argument is a world or player
        manager.getCommandContexts().registerContext(PlayerWorldContext.class, c ->{
            String arg1 = c.getArgs().get(0);
            World world = Bukkit.getWorld(arg1);
            Player player = Bukkit.getPlayer(arg1);
            if(world != null){
                var context = new PlayerWorldContext();
                context.setWorld(true);
                context.setWorld(world);
                return context;
            }else if(player != null){
                var context = new PlayerWorldContext();
                context.setPlayer(true);
                context.setPlayer(player);
                return context;
            }
            throw new InvalidCommandArgument(true);
        });
        configHandler = new ConfigHandler(this);
        cooldowns = new HashMap<>();
        saveDefaultConfig();
        manager.registerCommand(new TeleportCommand(this));
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
        getLogger().info("Unregistering commands...");
        manager.unregisterCommands();
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
