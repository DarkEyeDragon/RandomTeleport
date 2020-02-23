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

@CommandAlias("rtp|randomtp|randomteleport")
public class Teleport extends BaseCommand {

    private LocationHelper locationHelper;
    private ConfigHandler configHandler;
    private RandomTeleport plugin;

    public Teleport(RandomTeleport plugin) {
        this.plugin = plugin;
        configHandler = plugin.getConfigHandler();
        locationHelper = new LocationHelper(plugin);
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

    @Subcommand("reload")
    @CommandPermission("rtp.reload")
    public void onReload(CommandSender commandSender){
        plugin.reloadConfig();
    }

    private void teleport(Player player, boolean force) {
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
        locationHelper.getRandomLocation(player.getWorld(), configHandler.getRadius()).thenAccept(loc -> {
            Location location = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5,2,0.5);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            player.teleportAsync(location);
            player.sendMessage(configHandler.getTeleportMessage());
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
