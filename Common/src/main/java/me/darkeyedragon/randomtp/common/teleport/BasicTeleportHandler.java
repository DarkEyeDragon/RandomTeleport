package me.darkeyedragon.randomtp.common.teleport;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import me.darkeyedragon.randomtp.api.failsafe.DeathTracker;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.scheduler.TaskIdentifier;
import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportHandler;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.teleport.TeleportType;
import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.world.WorldHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class BasicTeleportHandler implements TeleportHandler {

    private final RandomTeleportPlugin<?> plugin;
    private final RandomConfigHandler configHandler;
    private final EcoHandler ecoHandler;

    /**
     * @param plugin the plugin.
     */
    public BasicTeleportHandler(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
        this.ecoHandler = plugin.getEcoHandler();
    }

    @Override
    public TeleportResponse toRandomLocation(TeleportProperty property) {
        long cooldown = getCooldown(property);

        if (cooldown > 0) {
            plugin.getMessageHandler().sendMessage(property.getTarget(), configHandler.getSectionMessage().getCountdown(cooldown / 50));
            return new BasicTeleportResponse(TeleportType.COOLDOWN);
        }
        return delayTimer(property);
    }

    private long getCooldown(TeleportProperty property) {
        CooldownHandler cooldownHandler = plugin.getCooldownHandler();
        RandomCooldown cooldown = cooldownHandler.getCooldown(property.getTarget().getUniqueId());
        if (cooldown != null && !property.isBypassCooldown()) {
            return cooldown.getRemainingTime();
        } else {
            return -1;
        }
    }

    private TeleportResponse delayTimer(TeleportProperty property) {
        long delay = property.getDelay();
        boolean cancelOnMove = property.getCancelOnMove();
        RandomPlayer player = property.getTarget();
        double price = property.getPrice();

        if (!property.isBypassEco() && price > 0) {
            if (ecoHandler != null) {
                if (!ecoHandler.hasEnough(player.getUniqueId(), price)) {
                    plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getSubSectionEconomy().getInsufficientFunds());
                    return new BasicTeleportResponse(TeleportType.INSUFFICIENT_FUNDS);
                } else {
                    String currency;
                    if (price > 1) {
                        currency = ecoHandler.getCurrencyPlural();
                    } else {
                        currency = ecoHandler.getCurrencySingular();
                    }
                    if (ecoHandler.makePayment(player.getUniqueId(), price)) {
                        plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getSubSectionEconomy().getPayment(price, currency));
                    }
                }
            } else {
                plugin.getMessageHandler().sendMessage(property.getCommandIssuer(), "<red>Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
                plugin.getLogger().severe("Economy based features are disabled. Vault not found. Set the rtp cost to 0 or install vault.");
            }
        }
        if (delay > 0) {
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            TaskIdentifier<?> taskId = plugin.getScheduler().runTaskLater(() -> {
                complete.set(true);
                teleport(property);
            }, delay).getTaskId();
            RandomLocation originalLoc = player.getLocation().clone();
            if (cancelOnMove) {
                //Cancel the teleport task if the player moves
                plugin.getScheduler().runTaskTimer(task -> {
                    RandomLocation currentLoc = player.getLocation();
                    if (complete.get()) {
                        task.cancel();
                    } else if ((originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ())) {
                        plugin.getScheduler().cancelTask(taskId);
                        task.cancel();
                        plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getTeleportCanceled());
                    }
                }, 0, 5L);
            }
        } else {
            teleport(property);
            return new BasicTeleportResponse(TeleportType.SUCCESS);
        }
        return new BasicTeleportResponse(TeleportType.FAIL);
    }

    private void addToDeathTimer(RandomPlayer player) {
        DeathTracker tracker = plugin.getDeathTracker();
        tracker.remove(player);
        plugin.getDeathTracker().add(player, configHandler.getSectionTeleport().getDeathTimer());
    }

    private void drawWarpParticles(RandomPlayer player, RandomParticle particle) {
        if (particle.getId().equalsIgnoreCase("none")) return;
        RandomLocation spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(particle.getId(), spawnLoc, particle.getAmount());
    }

    private void teleport(TeleportProperty property) {

        if (configHandler.getSectionDebug().isShowExecutionTimes()) {
            plugin.getLogger().info("Debug: teleport setup took " + (System.currentTimeMillis() - property.getInitTime()) + "ms");
        }
        RandomLocation location = plugin.getWorldHandler().getWorldQueue().popLocation(property.getWorld());
        if (location == null) {
            plugin.getMessageHandler().sendMessage(property.getCommandIssuer(), configHandler.getSectionMessage().getEmptyQueue());
            return;
        }

        //Minecraft 1.18 seems to have issues with large (chunk) locations causing it to crash.
        // Setting this to false will teleport on the main thread preventing this issue
        if (plugin.getConfigHandler().getSectionTeleport().isUseAsyncChunkTeleport()) {
            location.getWorld().getChunkAtAsync(location.getWorld(), location.getBlockX(), location.getBlockZ()).thenAcceptAsync(chunkSnapshot -> teleportLogic(property, location));
        } else {
            teleportLogic(property, location);
        }

    }

    private void teleportLogic(TeleportProperty property, RandomLocation location) {
        RandomPlayer player = property.getTarget();
        RandomParticle particle = property.getParticle();

        LocationSearcher baseLocationSearcher = WorldHandler.getLocationSearcher(property.getWorld().getEnvironment());
        if (!baseLocationSearcher.isSafe(location)) {
            toRandomLocation(property);
            return;
        }
        plugin.getCooldownHandler().addCooldown(player, new BasicCooldown(player.getUniqueId(), System.currentTimeMillis(), configHandler.getSectionTeleport().getCooldown() * 50));
        drawWarpParticles(player, particle);
        player.teleport(location.clone().add(0.5, 1.5, 0.5));
        drawWarpParticles(player, particle);
        //If deathtimer is enabled add it to the collection
        if (configHandler.getSectionTeleport().getDeathTimer() > 0) {
            addToDeathTimer(player);
        }
        plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getTeleport(location));
        if (plugin.hasConsent()) {
            plugin.getStats().addTeleportStat();
        }
        //TODO implement event pipeline
            /*RandomTeleportCompletedEvent event = new RandomTeleportCompletedEvent(player, property);
            Bukkit.getServer().getPluginManager().callEvent(event);*/
        //Generate a new location after the init delay
            /*plugin.getScheduler().runTaskLater(() -> {
                RandomWorld randomWorld = property.getLocation().getWorld();
                ConfigWorld configWorld = configHandler.getSectionWorld().getConfigWorld(randomWorld.getName());
                try {
                    plugin.getWorldHandler().generate(configWorld, randomWorld);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warn(ex.getMessage());
                }
            }, configHandler.getSectionQueue().getInitDelay());*/
    }
}


