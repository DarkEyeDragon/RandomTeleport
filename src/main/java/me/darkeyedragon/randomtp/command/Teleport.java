package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.util.LocationHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

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
    public void onTeleportOther(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            teleport(target, true);
        } else {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
        }
    }

    private void teleport(Player player, boolean force) {
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
        player.setWalkSpeed(1.2F);
        player.sendMessage(initMessage);
        Location loc;
        do {
            loc = locationHelper.pickRandom(player.getWorld(), Integer.MAX_VALUE / 100);
        } while (!locationHelper.isSafe(loc));
        player.teleportAsync(loc);
        plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
        player.sendMessage("X:" + loc.getBlockX() + " Y:" + loc.getBlockY() + " Z:" + loc.getBlockZ());
    }
}
