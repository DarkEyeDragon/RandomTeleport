package me.darkeyedragon.randomtp.sponge.command.completion;

import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.SpongeCommandCompletionContext;
import co.aikar.commands.SpongeCommandManager;
import com.google.common.collect.ImmutableList;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.command.context.PlayerWorldContext;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Registrar {

    private static Set<String> particleNames;

    public static void registerCompletions(SpongeCommandManager manager, AddonManager addonManager, RandomConfigHandler configHandler) {
        CommandCompletions<SpongeCommandCompletionContext> completions = manager.getCommandCompletions();
        completions.registerAsyncCompletion("addonFiles", context -> ImmutableList.copyOf(addonManager.getFileNames()));
        completions.registerAsyncCompletion("addonNames", context -> ImmutableList.copyOf(addonManager.getAddons().keySet()));
        completions.registerAsyncCompletion("particles", context -> getParticleNames());
        completions.registerAsyncCompletion("filteredWorlds", context -> getFilteredWorlds(configHandler, context));
        completions.registerAsyncCompletion("playerFilteredWorlds", context -> {
            List<String> filteredWorlds = getFilteredWorlds(configHandler, context);
            //Add all online players to the filtered worlds.
            filteredWorlds.addAll(Sponge.getServer().getOnlinePlayers().stream().map(User::getName).collect(Collectors.toList()));
            return filteredWorlds;
        });
    }

    private static List<String> getFilteredWorlds(RandomConfigHandler configHandler, SpongeCommandCompletionContext context) {
        Set<ConfigWorld> configWorlds = configHandler.getSectionWorld().getConfigWorlds();
        Iterator<ConfigWorld> randomWorldIterator = configWorlds.iterator();
        while (randomWorldIterator.hasNext()) {
            ConfigWorld configWorld = randomWorldIterator.next();
            if (configWorld.isNeedsWorldPermission() && !(context.getSource() instanceof ConsoleSource)) {
                if (!context.getPlayer().hasPermission("rtp.world." + configWorld.getName())) {
                    randomWorldIterator.remove();
                }
            }
        }
        return configWorlds.stream().map(ConfigWorld::getName).collect(Collectors.toList());
    }

    public static void registerContexts(SpongeCommandManager manager, RandomWorldHandler worldHandler, PlayerHandler playerHandler) {
        manager.getCommandContexts().registerContext(PlayerWorldContext.class, c -> {
            String arg1 = c.popFirstArg();
            RandomWorld randomWorld = worldHandler.getWorld(arg1);
            RandomPlayer player = playerHandler.getPlayer(arg1);
            PlayerWorldContext context = new PlayerWorldContext();
            if (randomWorld == null && player == null) {
                throw new InvalidCommandArgument(true);
            }
            if (randomWorld != null) {
                context.setWorld(randomWorld);
            } else {
                context.addPlayer(player);
            }
            return context;
        });
        manager.getCommandContexts().registerContext(RandomWorld.class, c -> {
            String arg1 = c.popFirstArg();
            return worldHandler.getWorld(arg1);
        });
        manager.getCommandContexts().registerContext(RandomPlayer.class, c -> {
            String arg1 = c.popFirstArg();
            return playerHandler.getPlayer(arg1);
        });
    }

    public static Collection<String> getParticleNames() {
        Collection<ParticleType> particleTypes = Sponge.getRegistry().getAllOf(ParticleType.class);
        if (particleNames == null)
            particleNames = particleTypes.stream().map(CatalogType::getName).collect(Collectors.toSet());
        return particleNames;
    }
}
