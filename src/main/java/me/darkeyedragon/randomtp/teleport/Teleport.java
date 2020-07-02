package me.darkeyedragon.randomtp.teleport;

import io.papermc.lib.PaperLib;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.eco.EcoHandler;
import me.darkeyedragon.randomtp.world.location.LocationSearcher;
import me.darkeyedragon.randomtp.world.location.LocationSearcherFactory;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleport {

    private final RandomTeleport plugin;
    private CommandSender commandSender;
    private Player player;
    private World world;
    private Location location;
    private boolean ignoreTeleportDelay;
    private boolean useEco;
    private ConfigHandler configHandler;
    private EcoHandler ecoHandler;
    private long cooldown;

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

    public Teleport location(Location location) {
        this.location = location;
        return this;
    }

    public Teleport ignoreTeleportDelay() {
        ignoreTeleportDelay = true;
        return this;
    }

    public Teleport useEco() {
        useEco = true;
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
        return teleport;
    }

    public void teleport() {
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
        if (cooldown > 0) {
            long lastTp = plugin.getCooldowns().get(player.getUniqueId());
            long remaining = lastTp + cooldown - System.currentTimeMillis();
            boolean ableToTp = remaining < 0;
            if (!ableToTp) {
                player.spigot().sendMessage(configHandler.getConfigMessage().getCountdown(remaining));
                return;
            }
        }
        if (delay == 0) {
            PaperLib.getChunkAtAsync(location).thenAccept(chunk -> {
                Block block = chunk.getWorld().getBlockAt(location);
                LocationSearcher locationSearcher = LocationSearcherFactory.getLocationSearcher(world, plugin);
                //TODO revalidate locations
                Location location = block.getLocation().add(0.5, 2, 0.5);
                plugin.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis());
                drawWarpParticles(player);
                PaperLib.teleportAsync(player, location);
                if (useEco) {
                    ecoHandler.makePayment(player, configHandler.getConfigEconomy().getPrice());
                    player.spigot().sendMessage(configHandler.getConfigMessage().getEconomy().getPayment());
                }
                drawWarpParticles(player);
                player.spigot().sendMessage(configHandler.getConfigMessage().getTeleport());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
                        plugin.getWorldQueue().get(world).generate(worldConfigSection, 1);
                    }
                }.runTaskLater(plugin, configHandler.getConfigQueue().getInitDelay());
            });
        }
    }

    private void drawWarpParticles(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection());
        player.getWorld().spawnParticle(Particle.CLOUD, spawnLoc, 20);
    }
}


