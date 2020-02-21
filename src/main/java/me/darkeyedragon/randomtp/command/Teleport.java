package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.destroystokyo.paper.ParticleBuilder;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.util.LocationHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDateTime;

@CommandAlias("rtp|randomtp|randomteleport")
public class Teleport extends BaseCommand {

    private LocationHelper locationHelper = new LocationHelper();
    private ConfigHandler configHandler;
    private RandomTeleport plugin;

    public Teleport(RandomTeleport plugin) {
        this.plugin = plugin;
        configHandler = new ConfigHandler(plugin);
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    public void onTeleport(CommandSender sender) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "Please specify a user!");
            return;
        }
        teleport(player, false);
    }

    @CommandPermission("rtp.teleport.other")
    @CatchUnknown
    @CommandCompletion("@players")
    public void onTeleportOther(CommandSender sender, OnlinePlayer player) {
        Player target = player.player;
        if (target != null) {
            teleport(target, true);
        } else {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
        }
    }

    private void teleport(Player player, boolean force) {
        System.out.println(LocalDateTime.now());
        String initMessage = configHandler.getInitMessage();
        if (plugin.getCooldowns().containsKey(player.getUniqueId())) {
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
        locationHelper.getRandomLocation(player.getWorld(), Integer.MAX_VALUE / 100).thenAccept(loc -> {
            Location location = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5,2,0.5);
            System.out.println("Location from Teleport: " + location.toString());
            player.teleportAsync(location);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            player.sendMessage("X:" + location.getBlockX() + " Y:" + location.getBlockY() + " Z:" + location.getBlockZ());
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

    }

    private void drawWarpParticles(Player player) {
        ParticleBuilder builder = new ParticleBuilder(Particle.CLOUD);
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        builder.count(20)
                .extra(1)
                .location(spawnLoc)
                .spawn();
    }
}
