package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.config.data.ConfigMessage;
import me.darkeyedragon.randomtp.config.data.ConfigQueue;
import me.darkeyedragon.randomtp.config.data.ConfigWorld;
import me.darkeyedragon.randomtp.eco.EcoHandler;
import me.darkeyedragon.randomtp.location.LocationFactory;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import me.darkeyedragon.randomtp.world.LocationQueue;
import me.darkeyedragon.randomtp.world.QueueListener;
import me.darkeyedragon.randomtp.world.WorldQueue;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("rtp|randomtp|randomteleport")
public class TeleportCommand extends BaseCommand {

    //private LocationSearcher locationHelper;
    private ConfigHandler configHandler;
    private RandomTeleport plugin;
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
                    sender.sendMessage(configMessage.getNoWorldPermission(newWorld));
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
                        sender.sendMessage(configMessage.getNoWorldPermission(newWorld));
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
                        sender.sendMessage(configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(newWorld);
                    if (worldConfigSection == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && worldConfigSection.needsWorldPermission())) {
                        sender.sendMessage(configMessage.getNoWorldPermission(newWorld));
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
        teleportSetup(player, finalWorld, sender.hasPermission("rtp.teleport.bypass"));
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    public void onReload(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        try {
            plugin.getConfigHandler().reload();
        } catch (InvalidConfigurationException e) {
            commandSender.sendMessage(ChatColor.RED + "Could not reload config! Your config is not configured correctly. Check the console for details.");
            e.printStackTrace();
        }
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

    private void teleportSetup(Player player, World world, boolean force) {
        boolean useEco = configHandler.getConfigEconomy().useEco() && !player.hasPermission("rtp.eco.bypass");
        if (useEco) {
            double price = configHandler.getConfigEconomy().getPrice();
            if (!ecoHandler.hasEnough(player, price)) {
                player.sendMessage(configMessage.getEconomy().getInsufficientFunds());
                return;
            }
        }
        boolean hasBypassPermission = player.hasPermission("rtp.teleportdelay.bypass");

        if (plugin.getCooldowns().containsKey(player.getUniqueId()) && !player.hasPermission("rtp.teleport.bypass")) {
            long lasttp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lasttp + configHandler.getConfigTeleport().getCooldown() - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp && !force) {
                player.sendMessage(configMessage.getCountdown(remaining));
                return;
            }
        }
        long delay = configHandler.getConfigTeleport().getDelay();
        if (delay > 0 && !hasBypassPermission) {
            player.sendMessage(configMessage.getInitTeleportDelay(delay));
        }
        Location loc = worldQueue.popLocation(world);
        if (loc == null) {
            player.sendMessage(configMessage.getEmptyQueue());
            return;
        }
        teleport(player, loc, world, useEco);

        /*if (loc == null) {
            player.sendMessage(configHandler.getDepletedQueueMessage());
            worldQueue.popLocation(world);
            locationHelper.getRandomLocation(worldConfigSection).thenAccept(loc1 -> teleport(player, loc1, world));
        } else {
            teleport(player, loc, world);
        }*/
    }

    public void teleport(Player player, Location loc, World world, boolean useEco) {
        boolean hasBypassPermission = player.hasPermission("rtp.teleportdelay.bypass");
        long teleportDelay = hasBypassPermission ? 1 : configHandler.getConfigTeleport().getDelay();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(configHandler.getConfigMessage().getInit());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 5, false, false));
                PaperLib.getChunkAtAsync(loc).thenAccept(chunk -> {
                    Location location = chunk.getWorld().getHighestBlockAt(loc).getLocation().add(0.5, 2, 0.5);
                    plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
                    drawWarpParticles(player);
                    PaperLib.teleportAsync(player, location);
                    if (useEco) {
                        ecoHandler.makePayment(player, configHandler.getConfigEconomy().getPrice());
                        player.sendMessage(configMessage.getEconomy().getPayment());
                    }
                    drawWarpParticles(player);
                    player.sendMessage(configMessage.getTeleport());
                    teleportSuccess = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
                            worldQueue.get(world).generate(worldConfigSection, 1);
                        }
                    }.runTaskLater(plugin, configHandler.getConfigQueue().getInitDelay());
                });
            }
        };

        runnable.runTaskLater(plugin, teleportDelay);
        if (hasBypassPermission) return;
        Location originalLoc = player.getLocation().clone();
        if (teleportDelay > 0 && configHandler.getConfigTeleport().isCancelOnMove()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location currentLoc = player.getLocation();
                    if (originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ()) {
                        if (!isTeleportSuccess())
                            player.sendMessage(configMessage.getTeleportCanceled());
                        runnable.cancel();
                        cancel();
                    }
                    if (isTeleportSuccess()) {
                        cancel();
                        teleportSuccess = false;
                    }
                }
            }.runTaskTimer(plugin, 0, 5L);
        }
    }

    private void drawWarpParticles(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(Particle.CLOUD, spawnLoc, 20);
    }

    private boolean isTeleportSuccess() {
        return teleportSuccess;
    }
}
