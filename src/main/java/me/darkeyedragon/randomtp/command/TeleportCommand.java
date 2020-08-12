package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.config.data.ConfigMessage;
import me.darkeyedragon.randomtp.config.data.ConfigQueue;
import me.darkeyedragon.randomtp.config.data.ConfigWorld;
import me.darkeyedragon.randomtp.teleport.Teleport;
import me.darkeyedragon.randomtp.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.util.MessageUtil;
import me.darkeyedragon.randomtp.world.LocationQueue;
import me.darkeyedragon.randomtp.world.QueueListener;
import me.darkeyedragon.randomtp.world.WorldQueue;
import me.darkeyedragon.randomtp.world.location.LocationFactory;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("rtp|randomtp|randomteleport")
public class TeleportCommand extends BaseCommand {

    //private BaseLocationSearcher locationHelper;
    private ConfigHandler configHandler;
    private final RandomTeleport plugin;
    private WorldQueue worldQueue;
    private LocationFactory locationFactory;
    private boolean teleportSuccess;

    //Economy

    //Config sections
    private ConfigMessage configMessage;
    private ConfigQueue configQueue;
    private ConfigWorld configWorld;

    public TeleportCommand(RandomTeleport plugin) {
        this.plugin = plugin;
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
    public void onTeleport(CommandIssuer issuer, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player;
        World newWorld;
        if (target == null) {
            if (issuer.isPlayer()) {
                player = issuer.getIssuer();
                newWorld = player.getWorld();
                if (!configWorld.contains(newWorld)) {
                    MessageUtil.sendMessage(issuer.getIssuer(), configMessage.getNoWorldPermission(newWorld));
                    return;
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.isPlayer()) {
                if (issuer.hasPermission("rtp.teleport.other")) {
                    player = target.getPlayer();
                    newWorld = world;
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(issuer.getIssuer(), configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                } else {
                    MessageUtil.sendMessage(issuer.getIssuer(), plugin.getManager().getLocales().getMessage(issuer, MessageKeys.PERMISSION_DENIED));
                    return;
                }
            } else if (target.isWorld()) {
                if (issuer.isPlayer()) {
                    player = issuer.getIssuer();
                    newWorld = target.getWorld();
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(issuer.getIssuer(), configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(newWorld);
                    if (worldConfigSection == null || ((!issuer.hasPermission("rtp.world." + newWorld.getName())) && worldConfigSection.needsWorldPermission())) {
                        MessageUtil.sendMessage(issuer.getIssuer(), configMessage.getNoWorldPermission(newWorld));
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
        final boolean useEco = configHandler.getConfigEconomy().useEco();
        final boolean bypassEco = player.hasPermission("rtp.eco.bypass");
        final boolean logic = useEco && !bypassEco;
        TeleportProperty teleportProperty = new TeleportProperty(issuer.getIssuer(), player, finalWorld, issuer.hasPermission("rtp.teleport.bypass"), issuer.hasPermission("rtp.teleportdelay.bypass"), logic, configHandler, configHandler.getConfigTeleport().getCooldown());
        Teleport teleport = new Teleport(plugin, teleportProperty);
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
        MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Clearing queue...");
        worldQueue.clear();
        MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Repopulating queue, this can take a while.");
        plugin.populateWorldQueue();
        MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @CommandCompletion("@worlds true|false true|false <Integer> <Integer> <Integer>")
    public void onAddWorld(CommandSender commandSender, World world, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            MessageUtil.sendMessage(commandSender, ChatColor.GOLD + "If " + ChatColor.AQUA + "useWorldBorder" + ChatColor.GOLD + " is false you need to provide the other parameters.");
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
                MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Successfully added to config.");
                locationQueue.subscribe(new QueueListener<Location>() {
                    @Override
                    public void onAdd(Location element) {
                        MessageUtil.sendMessage(commandSender, "Safe location added for " + ChatColor.GOLD + element.getWorld().getName() + ChatColor.GREEN + " (" + ChatColor.YELLOW + locationQueue.size() + ChatColor.GREEN + "/" + configQueue.getSize() + ")");
                        if (locationQueue.size() == configQueue.getSize()) {
                            MessageUtil.sendMessage(commandSender, "Queue populated for " + ChatColor.GOLD + element.getWorld().getName());
                            locationQueue.unsubscribe(this);
                        }
                    }

                    @Override
                    public void onRemove(Location element) {
                        //ignored
                    }
                });
            } else {
                MessageUtil.sendMessage(commandSender, ChatColor.RED + "Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            MessageUtil.sendMessage(commandSender, ChatColor.RED + "That world is already added to the list!");
        }
    }

    @Subcommand("removeworld")
    @CommandCompletion("@worlds")
    @CommandPermission("rtp.admin.removeworld")
    public void removeWorld(CommandSender commandSender, World world) {
        if (configWorld.contains(world)) {
            if (configWorld.remove(world)) {
                MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Removed world from the config and queue!");
            }
        } else {
            MessageUtil.sendMessage(commandSender, ChatColor.RED + "That world is not not in the config!");
        }
    }

    @Subcommand("resetcooldown")
    @CommandCompletion("@players")
    @CommandPermission("rtp.admin.resetcooldown")
    public void resetCooldown(CommandSender commandSender, OnlinePlayer target) {
        if (target != null) {
            Player player = target.getPlayer();
            if (plugin.getCooldowns().remove(player.getUniqueId()) != null) {
                MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Cooldown reset for " + player.getName());
            } else {
                MessageUtil.sendMessage(commandSender, ChatColor.RED + "There was no cooldown for " + player.getName());
            }
        }
    }

    @Subcommand("setprice")
    @CommandCompletion("<price>")
    @CommandPermission("rtp.admin.setprice")
    public void setPrice(CommandSender commandSender, double price) {
        if (price >= 0) {
            plugin.getConfigHandler().setTeleportPrice(price);
            MessageUtil.sendMessage(commandSender, ChatColor.GREEN + "Your price has been set");
        } else {
            MessageUtil.sendMessage(commandSender, ChatColor.RED + "Only positive numbers are allowed.");
        }
    }
}
