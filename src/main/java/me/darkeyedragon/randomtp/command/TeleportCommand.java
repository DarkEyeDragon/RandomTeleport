package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.util.LocationHelper;
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

    private LocationHelper locationHelper;
    private ConfigHandler configHandler;
    private RandomTeleport plugin;

    public TeleportCommand(RandomTeleport plugin) {
        this.plugin = plugin;
        configHandler = plugin.getConfigHandler();
        locationHelper = new LocationHelper(plugin);
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@players|@worlds")
    public void onTeleport(CommandSender sender, @Optional @CommandPermission("rtp.teleport.other") PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player;
        World newWorld;
        if(target == null){
            if (sender instanceof Player) {
                player = (Player) sender;
                newWorld = player.getWorld();
            }else{
                throw new InvalidCommandArgument(true);
            }
        }else{
            if(target.isPlayer()) {
                player = target.getPlayer();
                newWorld = world;
            }else if(target.isWorld()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    newWorld = target.getWorld();
                } else {
                    throw new InvalidCommandArgument(true);
                }
            }else{
                throw new InvalidCommandArgument(true);
            }
        }
        final World finalWorld = newWorld;
        //Add it to the Scheduler to not falsely trigger the "Moved to quickly" warning
        new BukkitRunnable() {
            @Override
            public void run() {
                teleport(player, finalWorld, sender.hasPermission("rtp.teleport.bypass"));
            }
        }.runTaskLater(plugin, 1);
    }

    @Subcommand("reload")
    @CommandPermission("rtp.reload")
    public void onReload(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded config");
    }

    private void teleport(Player player, World world, boolean force) {
        String initMessage = configHandler.getInitMessage();
        if (plugin.getCooldowns().containsKey(player.getUniqueId()) && !player.hasPermission("rtp.teleport.bypass")) {
            long lasttp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lasttp + configHandler.getCooldown() - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp && !force) {
                player.sendMessage(configHandler.getCountdownRemainingMessage(remaining));
                return;
            }
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, 5, false, false));
        player.sendMessage(initMessage);
        drawWarpParticles(player);
        locationHelper.getRandomLocation(world, configHandler.getRadius()).thenAccept(loc -> {
            Location location = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5, 2, 0.5);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            PaperLib.teleportAsync(player, location);
            player.sendMessage(configHandler.getTeleportMessage());
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private void drawWarpParticles(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(Particle.CLOUD, spawnLoc, 20);
    }
}
