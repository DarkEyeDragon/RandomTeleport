package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.config.data.ConfigMessage;
import me.darkeyedragon.randomtp.config.data.ConfigQueue;
import me.darkeyedragon.randomtp.config.data.ConfigWorld;
import me.darkeyedragon.randomtp.eco.EcoHandler;
import me.darkeyedragon.randomtp.teleport.Teleport;
import me.darkeyedragon.randomtp.world.LocationQueue;
import me.darkeyedragon.randomtp.world.QueueListener;
import me.darkeyedragon.randomtp.world.WorldQueue;
import me.darkeyedragon.randomtp.world.location.LocationFactory;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandAlias("rtp|randomtp|randomteleport")
public class TeleportCommand extends BaseCommand {

    //private LocationSearcher locationHelper;
    private ConfigHandler configHandler;
    private final RandomTeleport plugin;
    private WorldQueue worldQueue;
    private LocationFactory locationFactory;
    private boolean teleportSuccess;

    //Economy
    private final EcoHandler ecoHandler;

    //Config sections
    private ConfigMessage configMessage;
    private ConfigQueue configQueue;
    private ConfigWorld configWorld;

    public TeleportCommand(RandomTeleport plugin) {
        this.plugin = plugin;
        this.ecoHandler = plugin.getEcoHandler();
        setConfigs();
    }

    private void setConfigs() {
        this.configHandler = plugin.getConfigHandler();
        this.configMessage = configHandler.getConfigMessage();
        this.configQueue = configHandler.getConfigQueue();
        this.configWorld = configHandler.getConfigWorld();
        this.locationFactory = plugin.getLocationFactory();
        this.worldQueue = plugin.getWorldQueue();
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@players|@worlds")
    public void onTeleport(CommandSender sender, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player;
        World newWorld;
        if (target == null) {
            if (sender instanceof Player) {
                player = (Player) sender;
                newWorld = player.getWorld();
                if (!configWorld.contains(newWorld)) {
                    sender.spigot().sendMessage(configMessage.getNoWorldPermission(newWorld));
                    return;
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.isPlayer()) {
                if (sender.hasPermission("rtp.teleport.other")) {
                    player = target.getPlayer();
                    newWorld = world;
                    if (!configWorld.contains(newWorld)) {
                        sender.spigot().sendMessage(configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "I'm sorry, you do not have permission to perform this command!");
                    return;
                }
            } else if (target.isWorld()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    newWorld = target.getWorld();
                    if (!configWorld.contains(newWorld)) {
                        sender.spigot().sendMessage(configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(newWorld);
                    if (worldConfigSection == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && worldConfigSection.needsWorldPermission())) {
                        sender.spigot().sendMessage(configMessage.getNoWorldPermission(newWorld));
                        return;
                    }

                } else {
                    throw new InvalidCommandArgument(true);
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        }
        final World finalWorld = newWorld;
        Teleport teleport = new Teleport(plugin)
                .commandSender(sender)
                .configHandler(configHandler)
                .ecoHandler(ecoHandler)
                .world(finalWorld)
                .player(player)
                .cooldown(configHandler.getConfigTeleport().getCooldown())
                .bypassCooldown(sender.hasPermission("rtp.teleport.bypass"))
                .build();
        teleport.random();
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    public void onReload(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        plugin.getConfigHandler().reload();
        //Set the new config object references
        setConfigs();
        commandSender.sendMessage(ChatColor.GREEN + "Clearing queue...");
        worldQueue.clear();
        commandSender.sendMessage(ChatColor.GREEN + "Repopulating queue, this can take a while.");
        plugin.populateWorldQueue();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @CommandCompletion("@worlds true|false true|false <Integer> <Integer> <Integer>")
    public void onAddWorld(CommandSender commandSender, World world, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            commandSender.sendMessage(ChatColor.GOLD + "If " + ChatColor.AQUA + "useWorldBorder" + ChatColor.GOLD + " is false you need to provide the other parameters.");
            throw new InvalidCommandArgument(true);
        }
        if (!configWorld.contains(world)) {
            if (useWorldBorder) {
                if (radius == null) radius = 0;
                if (offsetX == null) offsetX = 0;
                if (offsetZ == null) offsetZ = 0;
            }
            LocationQueue locationQueue = configWorld.add(new WorldConfigSection(offsetX, offsetZ, radius, world, useWorldBorder, needsWorldPermission));
            if (locationQueue != null) {
                commandSender.sendMessage(ChatColor.GREEN + "Successfully added to config.");
                if (!(commandSender instanceof ConsoleCommandSender)) {
                    locationQueue.subscribe(new QueueListener<Location>() {
                        @Override
                        public void onAdd(Location element) {
                            commandSender.sendMessage("Safe location added for " + world.getName() + " (" + locationQueue.size() + "/" + configQueue.getSize() + ")");
                            if (locationQueue.size() == configQueue.getSize()) {
                                locationQueue.unsubscribe(this);
                            }
                        }

                        @Override
                        public void onRemove(Location element) {
                            //ignored
                        }
                    });
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "That world is already added to the list!");
        }
    }

    @Subcommand("removeworld")
    @CommandCompletion("@worlds")
    @CommandPermission("rtp.admin.removeworld")
    public void removeWorld(CommandSender commandSender, World world) {
        if (configWorld.contains(world)) {
            if (configWorld.remove(world)) {
                commandSender.sendMessage(ChatColor.GREEN + "Removed world from the config and queue!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "That world is not not in the config!");
        }
    }

    @Subcommand("resetcooldown")
    @CommandCompletion("@players")
    @CommandPermission("rtp.admin.resetcooldown")
    public void resetCooldown(CommandSender commandSender, Player target) {
        if (target != null) {
            if (plugin.getCooldowns().remove(target.getUniqueId()) != null) {
                commandSender.sendMessage(ChatColor.GREEN + "Cooldown reset for " + target.getName());
            } else {
                commandSender.sendMessage(ChatColor.RED + "There was no cooldown for " + target.getName());
            }
        }
    }

    @Subcommand("setprice")
    @CommandCompletion("<price>")
    @CommandPermission("rtp.admin.setprice")
    public void setPrice(CommandSender commandSender, double price) {
        if (price >= 0) {
            plugin.getConfigHandler().setTeleportPrice(price);
        } else {
            commandSender.sendMessage(ChatColor.RED + "Only positive numbers are allowed.");
        }
    }
}
