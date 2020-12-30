package me.darkeyedragon.randomtp.teleport;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.SpigotImpl;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.teleport.TeleportParticle;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;
import me.darkeyedragon.randomtp.event.RandomPreTeleportEvent;
import me.darkeyedragon.randomtp.event.RandomTeleportCompletedEvent;
import me.darkeyedragon.randomtp.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.stat.BStats;
import me.darkeyedragon.randomtp.util.MessageUtil;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Teleport {

    private final SpigotImpl impl;
    private final RandomTeleport plugin;
    private final TeleportProperty property;
    private final RandomConfigHandler configHandler;
    private final Player player;
    private final TeleportParticle<Particle> particle;
    private final EcoHandler ecoHandler;
    private final long cooldown;

    /**
     * @param impl the Spigot implementation class.
     * @param property the {@link TeleportProperty} used to get the teleport data from.
     */
    public Teleport(SpigotImpl impl, TeleportProperty property, TeleportParticle<Particle> particle) {
        this.impl = impl;
        this.plugin = impl.getInstance();
        this.property = property;
        this.configHandler = property.getConfigHandler();
        this.player = property.getPlayer();
        this.particle = particle;
        this.cooldown = configHandler.getSectionTeleport().getCooldown();
        this.ecoHandler = plugin.getEcoHandler();
    }

    public void random() {
        RandomPreTeleportEvent event = new RandomPreTeleportEvent(player, property);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        final long delay;
        double price = configHandler.getSectionEconomy().getPrice();
        if (property.isUseEco()) {
            if (ecoHandler != null) {
                if (!ecoHandler.hasEnough(player.getUniqueId(), price)) {
                    MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getSubSectionEconomy().getInsufficientFunds());
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
            delay = configHandler.getSectionTeleport().getDelay();
        }
        // Check if the player still has a cooldown active.
        if (cooldown > 0 && plugin.getCooldowns().containsKey(player.getUniqueId()) && !property.isBypassCooldown()) {
            long lastTp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lastTp + cooldown - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp) {
                MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getCountdown(remaining));
                return;
            }
        }
        //Initiate the delay timer if the delay is higher than 0
        if (delay > 0) {
            MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            int taskId = Bukkit.getScheduler().runTaskLater(impl, () -> {
                complete.set(true);
                teleport(player);
            }, delay).getTaskId();
            Location originalLoc = player.getLocation().clone();
            if (configHandler.getSectionTeleport().isCancelOnMove()) {
                //Cancel the teleport task if the player moves
                Bukkit.getScheduler().runTaskTimer(impl, bukkitTask -> {
                    Location currentLoc = player.getLocation();
                    if (complete.get()) {
                        bukkitTask.cancel();
                    } else if ((originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ())) {
                        Bukkit.getScheduler().cancelTask(taskId);
                        bukkitTask.cancel();
                        MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getTeleportCanceled());
                    }
                }, 0, 5L);
            }
        } else {
            teleport(player);
        }
    }

    private void addToDeathTimer(Player player) {
        DeathTracker tracker = plugin.getDeathTracker();
        if (tracker.contains(player)) {
            tracker.getBukkitTask(player).cancel();
            tracker.remove(player);
        }
        plugin.getDeathTracker().add(player, configHandler.getSectionTeleport().getDeathTimer());
    }

    private void drawWarpParticles(Player player, TeleportParticle<Particle> particle) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        Optional<Particle> optionalParticle = Optional.ofNullable(particle.getParticle());
        optionalParticle.ifPresent(presentParticle -> player.getWorld().spawnParticle(presentParticle, spawnLoc, particle.getAmount()));
    }

    private void teleport(Player player) {
        RandomLocation randomLocation = plugin.getWorldQueue().popLocation(property.getWorld());
        if (randomLocation == null) {
            MessageUtil.sendMessage(plugin, property.getCommandSender(), configHandler.getSectionMessage().getDepletedQueue());
            return;
        }
        Location location = WorldUtil.toLocation(randomLocation);
        PaperLib.getChunkAtAsync(location.getWorld(), location.getBlockX(), location.getBlockZ(), true, true ).thenAccept(chunk -> {
            LocationSearcher baseLocationSearcher = LocationSearcherFactory.getLocationSearcher(property.getWorld(), plugin);
            if (!baseLocationSearcher.isSafe(randomLocation)) {
                random();
                return;
            }
            Block block = chunk.getWorld().getBlockAt(WorldUtil.toLocation(randomLocation));
            Location loc = block.getLocation().add(0.5, 1.5, 0.5);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            drawWarpParticles(player, particle);
            PaperLib.teleportAsync(player, loc);
            //If deathtimer is enabled add it to the collection
            if (configHandler.getSectionTeleport().getDeathTimer() > 0) {
                addToDeathTimer(player);
            }
            if (property.isUseEco() && plugin.getEcoHandler() != null) {
                ecoHandler.makePayment(player.getUniqueId(), configHandler.getSectionEconomy().getPrice());
                MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getSubSectionEconomy().getPayment());
            }
            drawWarpParticles(player, particle);
            MessageUtil.sendMessage(plugin, player, configHandler.getSectionMessage().getTeleport(randomLocation));
            BStats.addTeleportStat();
            RandomTeleportCompletedEvent event = new RandomTeleportCompletedEvent(player, property);
            Bukkit.getServer().getPluginManager().callEvent(event);
            //Generate a new location after the init delay
            Bukkit.getScheduler().runTaskLater(plugin.getPlugin(), () -> {
                WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(property.getWorld());
                try {
                    plugin.getWorldQueue().get(property.getWorld()).generate(worldConfigSection, 1);
                } catch (IllegalArgumentException ex){
                    plugin.getLogger().warn(ex.getMessage());
                }
            }, configHandler.getSectionQueue().getInitDelay());
        });
    }
}


