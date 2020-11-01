package me.darkeyedragon.randomtp.teleport;

import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeleportProperty {

    private final CommandSender commandSender;
    private final Player player;
    private final RandomWorld world;
    private final boolean bypassCooldown;
    private final boolean ignoreTeleportDelay;
    private final boolean useEco;
    private final RandomConfigHandler configHandler;

    /**
     * @param commandSender       the {@link CommandSender} that executes the command.
     * @param player              the target to teleport. Can be null.
     * @param world               the {@link RandomWorld}.
     * @param bypassCooldown      whether the cooldown should be ignored when teleporting the {@link Player}.
     * @param ignoreTeleportDelay whether to ignore the teleport delay when teleporting the {@link Player}.
     * @param useEco              whether the {@link Player} should pay for the teleport.
     * @param configHandler       the {@link RandomConfigHandler}.
     */
    public TeleportProperty(@NotNull CommandSender commandSender, @Nullable Player player, @NotNull RandomWorld world, boolean bypassCooldown, boolean ignoreTeleportDelay, boolean useEco, @NotNull RandomConfigHandler configHandler) {
        this.commandSender = commandSender;
        this.player = player;
        this.world = world;
        this.bypassCooldown = bypassCooldown;
        this.ignoreTeleportDelay = ignoreTeleportDelay;
        this.useEco = useEco;
        this.configHandler = configHandler;
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

    public RandomConfigHandler getConfigHandler() {
        return configHandler;
    }

}
