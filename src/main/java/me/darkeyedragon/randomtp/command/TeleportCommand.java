package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.location.LocationFactory;
import me.darkeyedragon.randomtp.location.WorldConfigSection;
import me.darkeyedragon.randomtp.world.LocationQueue;
import me.darkeyedragon.randomtp.world.WorldQueue;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
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

    public TeleportCommand(RandomTeleport plugin) {
        this.plugin = plugin;
        configHandler = plugin.getConfigHandler();
        this.locationFactory = plugin.getLocationFactory();
        //locationHelper = plugin.getLocationSearcher();
        worldQueue = plugin.getWorldQueue();
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@players|@worlds")
    public void onTeleport(CommandSender sender, @Optional @CommandPermission("rtp.teleport.other") PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player;
        World newWorld;
        if (target == null) {
            if (sender instanceof Player) {
                player = (Player) sender;
                newWorld = player.getWorld();
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.isPlayer()) {
                player = target.getPlayer();
                newWorld = world;
            } else if (target.isWorld()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    newWorld = target.getWorld();
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
    @CommandPermission("rtp.reload")
    public void onReload(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        commandSender.sendMessage(ChatColor.GREEN + "Clearing queue...");
        worldQueue.clear();
        commandSender.sendMessage(ChatColor.GREEN + "Repopulating queue, this can take a while.");
        plugin.populateWorldQueue();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.addworld")
    @CommandCompletion("@worlds true|false true|false <Integer> <Integer> <Integer>")
    public void onAddWorld(CommandSender commandSender, World world, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            commandSender.sendMessage(ChatColor.GOLD + "If " + ChatColor.AQUA + "useWorldBorder" + ChatColor.GOLD + " is false you need to provide the other parameters.");
            return;
        }
        if (!configHandler.getWorlds().contains(world)) {
            if (radius == null) radius = 0;
            if (offsetX == null) offsetX = 0;
            if (offsetZ == null) offsetZ = 0;
            if (configHandler.addWorld(new WorldConfigSection(offsetX, offsetZ, radius, world, useWorldBorder, needsWorldPermission))) {
                commandSender.sendMessage(ChatColor.GREEN + "Successfully added to config.");
            } else {
                commandSender.sendMessage(ChatColor.RED + "Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "That world is already added to the list!");
        }
    }

    private void teleportSetup(Player player, World world, boolean force) {
        WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
        if (worldConfigSection == null || ((!player.hasPermission("rtp.world." + world.getName())) && worldConfigSection.needsWorldPermission())) {
            player.sendMessage(configHandler.getNoWorldPermissionMessage(world));
            return;
        }
        boolean hasBypassPermission = player.hasPermission("rtp.teleportdelay.bypass");

        if (configHandler.getWorldsBlacklist().contains(world)) {
            if (!configHandler.isWhitelist()) {
                player.sendMessage(configHandler.getBlacklistMessage());
                return;
            }
        }
        if (plugin.getCooldowns().containsKey(player.getUniqueId()) && !player.hasPermission("rtp.teleport.bypass")) {
            long lasttp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lasttp + configHandler.getCooldown() - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp && !force) {
                player.sendMessage(configHandler.getCountdownRemainingMessage(remaining));
                return;
            }
        }
        if (configHandler.getTeleportDelay() > 0 && !hasBypassPermission) {
            player.sendMessage(configHandler.getInitTeleportDelay());
        }
        LocationQueue locationQueue = worldQueue.get(world);
        Location loc = worldQueue.get(world).poll();
        System.out.println(locationQueue.size());
        teleport(player, loc, world);

        /*if (loc == null) {
            player.sendMessage(configHandler.getDepletedQueueMessage());
            worldQueue.popLocation(world);
            locationHelper.getRandomLocation(worldConfigSection).thenAccept(loc1 -> teleport(player, loc1, world));
        } else {
            teleport(player, loc, world);
        }*/
    }

    public void teleport(Player player, Location loc, World world) {
        boolean hasBypassPermission = player.hasPermission("rtp.teleportdelay.bypass");
        long teleportDelay = hasBypassPermission ? 1 : configHandler.getTeleportDelay();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(configHandler.getInitTeleportMessage());
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 5, false, false));
                Location location = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5, 2, 0.5);
                plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
                drawWarpParticles(player);
                PaperLib.teleportAsync(player, location);
                drawWarpParticles(player);
                player.sendMessage(configHandler.getTeleportMessage());
                teleportSuccess = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //LocationQueue locationQueue = new LocationQueue(configHandler.getQueueSize(), plugin.getLocationSearcher());
                        WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
                        //plugin.subscribe(locationQueue, world);
                        worldQueue.get(world).generate(worldConfigSection, 1);
                    }
                }.runTaskLater(plugin, configHandler.getInitDelay());
            }
        };

        runnable.runTaskLater(plugin, teleportDelay);
        if (hasBypassPermission) return;
        Location originalLoc = player.getLocation().clone();
        if (configHandler.getTeleportDelay() > 0 && configHandler.isCanceledOnMove()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location currentLoc = player.getLocation();
                    if (originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ()) {
                        if (!isTeleportSuccess())
                            player.sendMessage(configHandler.getCancelMessage());
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
