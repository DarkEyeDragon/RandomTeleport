package me.darkeyedragon.randomtp.teleport;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportProperty {

    private final CommandSender commandSender;
    private final Player player;
    private final RandomWorld world;
    private final boolean bypassCooldown;
    private final boolean ignoreTeleportDelay;
    private final boolean useEco;
    private final BukkitConfigHandler bukkitConfigHandler;
    private final long cooldown;

    public TeleportProperty(CommandSender commandSender, Player player, RandomWorld world, boolean bypassCooldown, boolean ignoreTeleportDelay, boolean useEco, BukkitConfigHandler bukkitConfigHandler, long cooldown) {
        this.commandSender = commandSender;
        this.player = player;
        this.world = world;
        this.bypassCooldown = bypassCooldown;
        this.ignoreTeleportDelay = ignoreTeleportDelay;
        this.useEco = useEco;
        this.bukkitConfigHandler = bukkitConfigHandler;
        this.cooldown = cooldown;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public Player getPlayer() {
        return player;
    }

    public RandomWorld getWorld() {
        return world;
    }

    public boolean isBypassCooldown() {
        return bypassCooldown;
    }

    public boolean isIgnoreTeleportDelay() {
        return ignoreTeleportDelay;
    }

    public boolean isUseEco() {
        return useEco;
    }

    public BukkitConfigHandler getConfigHandler() {
        return bukkitConfigHandler;
    }

    public long getCooldown() {
        return cooldown;
    }
}
