package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.teleport.*;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.block.RandomBlock;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.common.world.location.search.LocationSearcherFactory;

public class BasicTeleportHandler implements TeleportHandler {

    private final RandomTeleportPlugin<?> plugin;
    private final TeleportProperty property;
    private final RandomConfigHandler configHandler;
    private final RandomPlayer player;
    private final EcoHandler ecoHandler;

    /**
     * @param plugin   the plugin.
     * @param property the {@link TeleportProperty} used to get the teleport data from.
     */
    public BasicTeleportHandler(RandomTeleportPlugin<?> plugin, TeleportProperty property) {
        this.plugin = plugin;
        this.property = property;
        this.configHandler = plugin.getConfigHandler();
        this.player = property.getTarget();
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
                    return new BasicTeleportResponse(TeleportType.FAIL);
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
        RandomCooldown randomCooldown = cooldownHandler.getCooldown(player.getUniqueId());
        if (randomCooldown != null
                && configHandler.getSectionTeleport().getCooldown() > 0
                && !randomCooldown.isExpired()
                && !property.isBypassCooldown()
        ) {
            long remaining = randomCooldown.getRemainingTime();
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getCountdown(remaining));
            return new BasicTeleportResponse(TeleportType.COOLDOWN);
        }
        //TODO figure out scheduler
        //Initiate the delay timer if the delay is higher than 0
        /*if (delay > 0) {
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            int taskId = plugin.getScheduler().runTaskLater(plugin, () -> {
                complete.set(true);
                teleport(player);
            }, delay).getTaskId();
            RandomLocation originalLoc = player.getLocation().clone();
            if (configHandler.getSectionTeleport().isCancelOnMove()) {
                //Cancel the teleport task if the player moves
                plugin.getScheduler().runTaskTimer(plugin, bukkitTask -> {
                    RandomLocation currentLoc = player.getLocation();
                    if (complete.get()) {
                        bukkitTask.cancel();
                    } else if ((originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ())) {
                        plugin.getScheduler().cancelTask(taskId);
                        bukkitTask.cancel();
                        plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getTeleportCanceled());
                    }
                }, 0, 5L);
            }
        } else {*/
        teleport(player);
        return new BasicTeleportResponse(TeleportType.SUCCESS);
        //}
    }

    private void addToDeathTimer(RandomPlayer player) {
        DeathTracker tracker = plugin.getDeathTracker();
        tracker.remove(player);
        plugin.getDeathTracker().add(player, configHandler.getSectionTeleport().getDeathTimer());
    }

    private void drawWarpParticles(RandomPlayer player, RandomParticle<?> particle) {
        RandomLocation spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(particle, spawnLoc, particle.getAmount());
    }

    private void teleport(RandomPlayer player) {
        RandomLocation randomLocation = plugin.getWorldHandler().getWorldQueue().popLocation(property.getWorld());
        if (randomLocation == null) {
            plugin.getMessageHandler().sendMessage(property.getCommandIssuer(), configHandler.getSectionMessage().getDepletedQueue());
            return;
        }
        //getChunkAtAsync(location.getWorld(), location.getX(), location.getZ(), true, true)
        randomLocation.getWorld().getChunkAtAsync(randomLocation.getWorld(), randomLocation.getBlockX(), randomLocation.getBlockZ()).thenAccept(chunk -> {
            LocationSearcher baseLocationSearcher = LocationSearcherFactory.getLocationSearcher(property.getLocation().getWorld(), plugin);
            if (!baseLocationSearcher.isSafe(randomLocation)) {
                toRandomLocation(player);
                return;
            }
            RandomBlock block = chunk.getWorld().getBlockAt(randomLocation);
            RandomLocation loc = block.getLocation().add(0.5, 1.5, 0.5);
            plugin.getCooldownHandler().addCooldown(player, new BasicCooldown(player.getUniqueId(), System.currentTimeMillis(), configHandler.getSectionTeleport().getCooldown()));
            drawWarpParticles(player, property.getParticle());
            player.teleportAsync(property);
            //PaperLib.teleportAsync(player, loc);
            //If deathtimer is enabled add it to the collection
            if (configHandler.getSectionTeleport().getDeathTimer() > 0) {
                addToDeathTimer(player);
            }
            if (!property.isBypassEco() && plugin.getEcoHandler() != null) {
                ecoHandler.makePayment(player.getUniqueId(), configHandler.getSectionEconomy().getPrice());
                plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getSubSectionEconomy().getPayment());
            }
            drawWarpParticles(player, property.getParticle());
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getTeleport(randomLocation));
            plugin.getStats().addTeleportStat();
            //TODO implement event pipeline
            /*RandomTeleportCompletedEvent event = new RandomTeleportCompletedEvent(player, property);
            Bukkit.getServer().getPluginManager().callEvent(event);*/
            //Generate a new location after the init delay
            plugin.getScheduler().runTaskLater(() -> {
                RandomWorld randomWorld = property.getLocation().getWorld();
                SectionWorldDetail sectionWorldDetail = configHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
                try {
                    plugin.getWorldHandler().getWorldQueue().get(randomWorld).generate(sectionWorldDetail, 1);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warn(ex.getMessage());
                }
            }, configHandler.getSectionQueue().getInitDelay());
        });
    }
}


