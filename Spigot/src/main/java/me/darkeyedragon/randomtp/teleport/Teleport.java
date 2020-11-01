package me.darkeyedragon.randomtp.teleport;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.SpigotImpl;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.util.MessageUtil;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class Teleport {

    private final SpigotImpl impl;
    private final RandomTeleport plugin;
    private final TeleportProperty property;
    private final BukkitConfigHandler bukkitConfigHandler;
    private final Player player;
    private EcoHandler ecoHandler;

    public Teleport(SpigotImpl impl, TeleportProperty property) {
        this.impl = impl;
        this.plugin = impl.getInstance();
        this.property = property;
        this.bukkitConfigHandler = property.getConfigHandler();
        this.player = property.getPlayer();
    }

    public void random() {
        final long delay;
        double price = bukkitConfigHandler.getSectionEconomy().getPrice();
        if (property.isUseEco()) {
            ecoHandler = plugin.getEcoHandler();
            if (ecoHandler != null) {
                if (!ecoHandler.hasEnough(player.getUniqueId(), price)) {
                    MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getSubSectionEconomy().getInsufficientFunds());
                    return;
                }
            } else {
                MessageUtil.sendMessage(plugin, property.getCommandSender(), ChatColor.RED + "Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
                Bukkit.getLogger().severe("Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
            }
        }
        //Teleport instantly if the command sender has bypass permission
        if (property.isIgnoreTeleportDelay()) {
            delay = 0;
        } else {
            delay = bukkitConfigHandler.getSectionTeleport().getDelay();
        }
        // Check if the player still has a cooldown active.
        if (property.getCooldown() > 0 && plugin.getCooldowns().containsKey(player.getUniqueId()) && !property.isBypassCooldown()) {
            long lastTp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lastTp + property.getCooldown() - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp) {
                MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getCountdown(remaining));
                return;
            }
        }
        if (delay == 0) {
            teleport();
        } else {
            MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            int taskId = Bukkit.getScheduler().runTaskLater(impl, () -> {
                complete.set(true);
                teleport();
            }, delay).getTaskId();
            Location originalLoc = player.getLocation().clone();
            if (bukkitConfigHandler.getSectionTeleport().isCancelOnMove()) {
                Bukkit.getScheduler().runTaskTimer(impl, bukkitTask -> {
                    Location currentLoc = player.getLocation();
                    if (complete.get()) {
                        bukkitTask.cancel();
                    } else if ((originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ())) {
                        Bukkit.getScheduler().cancelTask(taskId);
                        bukkitTask.cancel();
                        MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getTeleportCanceled());
                    }
                }, 0, 5L);
            }
        }
    }

    private void addToDeathTimer(Player player) {
        DeathTracker tracker = plugin.getDeathTracker();
        if (tracker.contains(player)) {
            tracker.getBukkitTask(player).cancel();
            tracker.remove(player);
        }
        plugin.getDeathTracker().add(player, bukkitConfigHandler.getSectionTeleport().getDeathTimer());
    }

    private void drawWarpParticles(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(Particle.CLOUD, spawnLoc, 20);
    }

    private void teleport() {
        RandomLocation randomLocation = plugin.getWorldQueue().popLocation(property.getWorld());
        if (randomLocation == null) {
            MessageUtil.sendMessage(plugin, property.getCommandSender(), bukkitConfigHandler.getSectionMessage().getDepletedQueue());
            return;
        }
        Location location = WorldUtil.toLocation(randomLocation);
        PaperLib.getChunkAtAsync(location).thenAccept(chunk -> {
            LocationSearcher baseLocationSearcher = LocationSearcherFactory.getLocationSearcher(property.getWorld(), plugin);
            if (!baseLocationSearcher.isSafe(randomLocation)) {
                random();
                return;
            }
            Block block = chunk.getWorld().getBlockAt(WorldUtil.toLocation(randomLocation));
            Location loc = block.getLocation().add(0.5, 1.5, 0.5);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            drawWarpParticles(player);
            PaperLib.teleportAsync(player, loc);
            if (bukkitConfigHandler.getSectionTeleport().getDeathTimer() > 0) {
                addToDeathTimer(player);
            }
            if (property.isUseEco() && plugin.getEcoHandler() != null) {
                ecoHandler.makePayment(player.getUniqueId(), bukkitConfigHandler.getSectionEconomy().getPrice());
                MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getSubSectionEconomy().getPayment());
            }
            drawWarpParticles(player);
            MessageUtil.sendMessage(plugin, player, bukkitConfigHandler.getSectionMessage().getTeleport(randomLocation));
            Bukkit.getScheduler().runTaskLater(plugin.getPlugin(), () -> {
                WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(property.getWorld());
                plugin.getWorldQueue().get(property.getWorld()).generate(worldConfigSection, 1);
            }, bukkitConfigHandler.getSectionQueue().getInitDelay());
        });
    }
}


