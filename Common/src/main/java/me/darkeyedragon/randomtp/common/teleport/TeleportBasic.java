package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.teleport.*;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class TeleportBasic implements TeleportHandler {

    private final RandomTeleportPlugin<?> plugin;
    private final TeleportProperty property;
    private final RandomConfigHandler configHandler;
    private final RandomPlayer player;
    private final RandomParticle<?> particle;
    private final EcoHandler ecoHandler;
    private final long cooldown;

    /**
     * @param plugin   the plugin.
     * @param property the {@link TeleportProperty} used to get the teleport data from.
     */
    public TeleportBasic(RandomTeleportPlugin<?> plugin, TeleportProperty property, RandomParticle<?> particle) {
        this.plugin = plugin;
        this.property = property;
        this.configHandler = plugin.getConfigHandler();
        this.player = property.getTarget();
        this.particle = particle;
        this.cooldown = configHandler.getSectionTeleport().getCooldown();
        this.ecoHandler = plugin.getEcoHandler();
    }

    @Override
    public TeleportResponse toRandomLocation(RandomPlayer randomPlayer) {
        //TODO Implement event pipeline
        /*RandomPreTeleportEvent event = new RandomPreTeleportEvent(player, property);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }*/
        final long delay;
        double price = configHandler.getSectionEconomy().getPrice();
        if (!property.isBypassEco()) {
            if (ecoHandler != null) {
                if (!ecoHandler.hasEnough(player.getUniqueId(), price)) {
                    plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getSubSectionEconomy().getInsufficientFunds());
                    return;
                }
            } else {
                plugin.getMessageHandler().sendMessage(property.getCommandIssuer(), "<red>Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
                plugin.getLogger().severe("Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
            }
        }
        //Teleport instantly if the command sender has bypass permission
        if (property.isBypassTeleportDelay()) {
            delay = 0;
        } else {
            delay = configHandler.getSectionTeleport().getDelay();
        }
        // Check if the player still has a cooldown active.
        CooldownHandler cooldownHandler = plugin.getCooldownHandler();
        if (cooldown > 0 && cooldownHandler.getPlayer(player.getUniqueId()) != null && !property.isBypassCooldown()) {
            RandomCooldown randomCooldown = plugin.getCooldownHandler().getCooldown(player.getUniqueId());
            if (!randomCooldown.isExpired()) {
                plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getCountdown(remaining));
                return;
            }
        }
        //Initiate the delay timer if the delay is higher than 0
        if (delay > 0) {
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                complete.set(true);
                teleport(player);
            }, delay).getTaskId();
            RandomLocation originalLoc = player.getLocation().clone();
            if (configHandler.getSectionTeleport().isCancelOnMove()) {
                //Cancel the teleport task if the player moves
                Bukkit.getScheduler().runTaskTimer(impl, bukkitTask -> {
                    RandomLocation currentLoc = player.getLocation();
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

    private void addToDeathTimer(RandomPlayer player) {
        DeathTracker tracker = plugin.getDeathTracker();
        if (tracker.contains(player)) {
            tracker.getBukkitTask(player).cancel();
            tracker.remove(player);
        }
        plugin.getDeathTracker().add(player, configHandler.getSectionTeleport().getDeathTimer());
    }

    private void drawWarpParticles(RandomPlayer player, TeleportParticle<Particle> particle) {
        RandomLocation spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        Optional<Particle> optionalParticle = Optional.ofNullable(particle.getParticle());
        optionalParticle.ifPresent(presentParticle -> player.getWorld().spawnParticle(presentParticle, spawnLoc, particle.getAmount()));
    }

    private void teleport(RandomPlayer player) {
        RandomLocation randomLocation = plugin.getWorldHandler().getWorldQueue().popLocation(property.getLocation().getWorld());
        if (randomLocation == null) {
            plugin.getMessageHandler().sendMessage(property.getCommandIssuer(), configHandler.getSectionMessage().getDepletedQueue());
            return;
        }
        //getChunkAtAsync(location.getWorld(), location.getX(), location.getZ(), true, true)
        randomLocation.getRandomWorld().getChunkAtAsync(randomLocation.getWorld(), randomLocation.getX(), randomLocation.getZ()).thenAccept(chunk -> {
            LocationSearcher baseLocationSearcher = LocationSearcherFactory.getLocationSearcher(property.getWorld(), plugin);
            if (!baseLocationSearcher.isSafe(randomLocation)) {
                random();
                return;
            }
            RandomBlock block = chunk.getWorld().getBlockAt(randomLocation);
            RandomLocation loc = block.getLocation().add(0.5, 1.5, 0.5);
            plugin.getCooldownHandler().addCooldown(new Spi) player.getUniqueId(), System.currentTimeMillis())
            drawWarpParticles(player, particle);
            player.teleportAsync(loc);
            //PaperLib.teleportAsync(player, loc);
            //If deathtimer is enabled add it to the collection
            if (configHandler.getSectionTeleport().getDeathTimer() > 0) {
                addToDeathTimer(player);
            }
            if (!property.isBypassEco() && plugin.getEcoHandler() != null) {
                ecoHandler.makePayment(player.getUniqueId(), configHandler.getSectionEconomy().getPrice());
                plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getSubSectionEconomy().getPayment());
            }
            drawWarpParticles(player, particle);
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getTeleport(randomLocation));
            plugin.getStats().addTeleportStat();
            //TODO implement event pipeline
            /*RandomTeleportCompletedEvent event = new RandomTeleportCompletedEvent(player, property);
            Bukkit.getServer().getPluginManager().callEvent(event);*/
            //Generate a new location after the init delay
            Bukkit.getScheduler().runTaskLater(plugin.getPlugin(), () -> {
                WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(property.getLocation().getWorld());
                try {
                    plugin.getWorldHandler().getWorldQueue().get(property.getLocation().getWorld()).generate(worldConfigSection, 1);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warn(ex.getMessage());
                }
            }, configHandler.getSectionQueue().getInitDelay());
        });
    }
}


