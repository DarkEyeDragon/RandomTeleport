package me.darkeyedragon.randomtp.command.completion;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.PlayerHandler;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.RandomWorldHandler;
import me.darkeyedragon.randomtp.common.addon.AddonManager;
import me.darkeyedragon.randomtp.common.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.world.PlayerSpigot;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class Registrar {
    public static void registerCompletions(PaperCommandManager manager, AddonManager addonManager, BukkitConfigHandler configHandler) {
        manager.getCommandCompletions().registerAsyncCompletion("addonFiles", context -> ImmutableList.copyOf(addonManager.getFileNames()));
        manager.getCommandCompletions().registerAsyncCompletion("addonNames", context -> ImmutableList.copyOf(addonManager.getAddons().keySet()));
        manager.getCommandCompletions().registerAsyncCompletion("particles", context -> Arrays.stream(Particle.values()).map(Particle::name).collect(Collectors.toList()));
        manager.getCommandCompletions().registerAsyncCompletion("filteredWorlds", context -> {
            Set<RandomWorld> randomWorlds = configHandler.getSectionWorld().getWorlds();
            Iterator<RandomWorld> randomWorldIterator = randomWorlds.iterator();
            while (randomWorldIterator.hasNext()) {
                RandomWorld randomWorld = randomWorldIterator.next();
                SectionWorldDetail sectionWorldDetail = configHandler.getSectionWorld().getSectionWorldDetail(randomWorld);
                if (sectionWorldDetail.needsWorldPermission() && !(context.getSender() instanceof ConsoleCommandSender)) {
                    if (!context.getPlayer().hasPermission("rtp.world." + randomWorld.getName())) {
                        randomWorldIterator.remove();
                    }
                }
            }
            return randomWorlds.stream().map(RandomWorld::getName).collect(Collectors.toList());
        });
    }

    public static void registerContexts(PaperCommandManager manager, RandomWorldHandler worldHandler, PlayerHandler playerHandler) {
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
            return new PlayerSpigot(Bukkit.getPlayer(arg1));
        });
    }
}
