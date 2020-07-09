package me.darkeyedragon.randomtp.teleport;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.eco.EcoHandler;
import me.darkeyedragon.randomtp.world.location.LocationSearcher;
import me.darkeyedragon.randomtp.world.location.LocationSearcherFactory;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class Teleport {

    private final RandomTeleport plugin;
    private CommandSender commandSender;
    private Player player;
    private World world;
    private boolean ignoreTeleportDelay;
    private boolean useEco;
    private ConfigHandler configHandler;
    private EcoHandler ecoHandler;
    private long cooldown;
    private boolean bypassCooldown;

    public Teleport(RandomTeleport plugin) {
        this.plugin = plugin;
    }

    public Teleport commandSender(CommandSender commandSender) {
        this.commandSender = commandSender;
        return this;
    }

    public Teleport player(Player player) {
        this.player = player;
        return this;
    }

    public Teleport world(World world) {
        this.world = world;
        return this;
    }

    public Teleport bypassCooldown(boolean bypassCooldown) {
        this.bypassCooldown = bypassCooldown;
        return this;
    }

    public Teleport ignoreTeleportDelay() {
        ignoreTeleportDelay = true;
        return this;
    }

    public Teleport ignoreTeleportDelay(boolean ignoreTeleportDelay) {
        this.ignoreTeleportDelay = ignoreTeleportDelay;
        return this;
    }

    public Teleport useEco(boolean useEco) {
        this.useEco = useEco;
        return this;
    }

    public Teleport configHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
        return this;
    }

    public Teleport ecoHandler(EcoHandler ecoHandler) {
        this.ecoHandler = ecoHandler;
        return this;
    }

    public Teleport cooldown(long cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public Teleport build() {
        Teleport teleport = new Teleport(this.plugin);
        teleport.configHandler = this.configHandler;
        teleport.ignoreTeleportDelay = this.ignoreTeleportDelay;
        teleport.player = this.player;
        teleport.world = this.world;
        teleport.useEco = this.useEco;
        teleport.commandSender = this.commandSender;
        teleport.cooldown = this.cooldown;
        teleport.bypassCooldown = this.bypassCooldown;
        teleport.ecoHandler = this.ecoHandler;
        return teleport;
    }

    public void random() {
        final long delay;
        if (useEco) {
            double price = configHandler.getConfigEconomy().getPrice();
            if (!ecoHandler.hasEnough(player, price)) {
                player.spigot().sendMessage(configHandler.getConfigMessage().getEconomy().getInsufficientFunds());
                return;
            }
        }
        //Teleport instantly if the command sender has bypass permission
        if (ignoreTeleportDelay) {
            delay = 0;
        } else {
            delay = configHandler.getConfigTeleport().getDelay();
        }
        // Check if the player still has a cooldown active.
        if (cooldown > 0 && plugin.getCooldowns().containsKey(player.getUniqueId()) && !bypassCooldown) {
            long lastTp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lastTp + cooldown - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp) {
                player.spigot().sendMessage(configHandler.getConfigMessage().getCountdown(remaining));
                return;
            }
        }
        if (delay == 0) {
            teleport();
        } else {
            player.spigot().sendMessage(configHandler.getConfigMessage().getInitTeleportDelay(delay));
            AtomicBoolean complete = new AtomicBoolean(false);
            int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                complete.set(true);
                teleport();
            }, delay).getTaskId();
            Location originalLoc = player.getLocation().clone();
            if (configHandler.getConfigTeleport().isCancelOnMove()) {
                Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
                    Location currentLoc = player.getLocation();
                    if (complete.get()) {
                        bukkitTask.cancel();
                    } else if ((originalLoc.getX() != currentLoc.getX() || originalLoc.getY() != currentLoc.getY() || originalLoc.getZ() != currentLoc.getZ())) {
                        Bukkit.getScheduler().cancelTask(taskId);
                        bukkitTask.cancel();
                        player.spigot().sendMessage(configHandler.getConfigMessage().getTeleportCanceled());
                    }
                }, 0, 5L);
            }
        }
    }

    private void drawWarpParticles(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(Particle.CLOUD, spawnLoc, 20);
    }

    private void teleport() {
        Location location = plugin.getWorldQueue().popLocation(world);
        if (location == null) {
            commandSender.spigot().sendMessage(configHandler.getConfigMessage().getDepletedQueue());
            return;
        }
        LocationSearcher locationSearcher = LocationSearcherFactory.getLocationSearcher(world, plugin);
        if (!locationSearcher.isSafeLocation(location)) {
            random();
        }
        PaperLib.getChunkAtAsync(location).thenAccept(chunk -> {
            Block block = chunk.getWorld().getBlockAt(location);
            Location loc = block.getLocation().add(0.5, 2, 0.5);
            plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
            drawWarpParticles(player);
            PaperLib.teleportAsync(player, loc);
            if (useEco) {
                ecoHandler.makePayment(player, configHandler.getConfigEconomy().getPrice());
                player.spigot().sendMessage(configHandler.getConfigMessage().getEconomy().getPayment());
            }
            drawWarpParticles(player);
            player.spigot().sendMessage(configHandler.getConfigMessage().getTeleport(location));
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
                plugin.getWorldQueue().get(world).generate(worldConfigSection, 1);
            }, configHandler.getConfigQueue().getInitDelay());
        });
    }
}


