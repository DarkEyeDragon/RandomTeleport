package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.SpigotImpl;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.Offset;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;
import me.darkeyedragon.randomtp.config.BukkitConfigHandler;
import me.darkeyedragon.randomtp.teleport.Teleport;
import me.darkeyedragon.randomtp.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.util.MessageUtil;
import me.darkeyedragon.randomtp.util.WorldUtil;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("rtp|randomtp|randomteleport")
public class TeleportCommand extends BaseCommand {

    //private BaseLocationSearcher locationHelper;
    private BukkitConfigHandler bukkitConfigHandler;
    private final RandomTeleport instance;
    private final SpigotImpl plugin;
    private WorldQueue worldQueue;
    private LocationFactory locationFactory;

    //Economy

    //Config sections
    private SectionMessage configMessage;
    private SectionQueue configQueue;
    private SectionWorld configWorld;

    public TeleportCommand(SpigotImpl plugin) {
        this.instance = plugin.getInstance();
        this.plugin = plugin;
        setConfigs();
    }

    private void setConfigs() {
        this.bukkitConfigHandler = instance.getConfigHandler();
        this.configMessage = bukkitConfigHandler.getSectionMessage();
        this.configQueue = bukkitConfigHandler.getSectionQueue();
        this.configWorld = bukkitConfigHandler.getSectionWorld();
        this.locationFactory = instance.getLocationFactory();
        this.worldQueue = instance.getWorldQueue();
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@players|@filteredWorlds")
    public void onTeleport(CommandSender sender, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player = null;
        List<Player> targets = new ArrayList<>();
        RandomWorld newWorld;
        if (target == null) {
            if (sender instanceof Player) {
                player = (Player) sender;
                newWorld = WorldUtil.toRandomWorld(player.getWorld());
                if (!configWorld.contains(newWorld)) {
                    MessageUtil.sendMessage(instance, sender, configMessage.getNoWorldPermission(newWorld));
                    return;
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.getPlayers().size() > 0) {
                if (sender.hasPermission("rtp.teleport.other")) {
                    targets = target.getPlayers();
                    newWorld = WorldUtil.toRandomWorld(world);
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(instance, sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                } else {
                    MessageUtil.sendMessage(instance, sender, ChatColor.RED + "I'm sorry, you do not have permission to perform this command!");
                    return;
                }
            } else if (target.isWorld()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    newWorld = target.getWorld();
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(instance, sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    WorldConfigSection worldConfigSection = instance.getLocationFactory().getWorldConfigSection(newWorld);
                    if (worldConfigSection == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && worldConfigSection.needsWorldPermission())) {
                        MessageUtil.sendMessage(instance, sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }

                } else {
                    throw new InvalidCommandArgument(true);
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        }
        final RandomWorld finalWorld = newWorld;
        if(player == null){
            for (Player target1 : targets) {
                teleport(sender, target1, finalWorld);
            }
        }else{
            teleport(sender, player, finalWorld);
        }
    }

    private void teleport(CommandSender sender, Player player, RandomWorld world) {
        final boolean useEco = bukkitConfigHandler.getSectionEconomy().useEco();
        final boolean bypassEco = player.hasPermission("rtp.eco.bypass");
        final boolean logic = useEco && !bypassEco;
        TeleportProperty teleportProperty = new TeleportProperty(sender, player, world, sender.hasPermission("rtp.teleport.bypass"), sender.hasPermission("rtp.teleportdelay.bypass"), logic, bukkitConfigHandler);
        Teleport teleport = new Teleport(plugin, teleportProperty, bukkitConfigHandler.getSectionTeleport().getParticle());
        teleport.random();
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        try {
            instance.getConfigHandler().reload();
        } catch (InvalidConfigurationException e) {
            MessageUtil.sendMessage(instance, sender, ChatColor.RED + "Your config is not configured properly. Error:");
            MessageUtil.sendMessage(instance, sender, ChatColor.RED + e.getMessage());
            e.printStackTrace();
        }
        //Set the new config object references
        setConfigs();
        MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Clearing queue...");
        worldQueue.clear();
        MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Repopulating queue, this can take a while.");
        instance.init();
        MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @CommandCompletion("@worlds true|false true|false <Integer> <Integer> <Integer>")
    public void onAddWorld(CommandSender sender, World world, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        RandomWorld randomWorld = WorldUtil.toRandomWorld(world);
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            MessageUtil.sendMessage(instance, sender, ChatColor.GOLD + "If " + ChatColor.AQUA + "useWorldBorder" + ChatColor.GOLD + " is false you need to provide the other parameters.");
            throw new InvalidCommandArgument(true);
        }
        if (!configWorld.contains(randomWorld)) {
            if (useWorldBorder) {
                if (radius == null) radius = 0;
                if (offsetX == null) offsetX = 0;
                if (offsetZ == null) offsetZ = 0;
            }
            configWorld.add(new WorldConfigSection(new Offset(offsetX, offsetZ, radius), randomWorld, useWorldBorder, needsWorldPermission));
            LocationQueue locationQueue = instance.getQueue(randomWorld);
            if (locationQueue != null) {
                MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Successfully added to config.");
                locationQueue.subscribe(new QueueListener<RandomLocation>() {
                    @Override
                    public void onAdd(RandomLocation element) {
                        MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Safe location added for " + ChatColor.GOLD + element.getWorld().getName() + ChatColor.GREEN + " (" + ChatColor.YELLOW + locationQueue.size() + ChatColor.GREEN + "/" + configQueue.getSize() + ")");
                        if (locationQueue.size() == configQueue.getSize()) {
                            MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Queue populated for " + ChatColor.GOLD + element.getWorld().getName());
                            locationQueue.unsubscribe(this);
                        }
                    }

                    @Override
                    public void onRemove(RandomLocation element) {
                        //ignored
                    }
                });
            } else {
                MessageUtil.sendMessage(instance, sender, ChatColor.RED + "Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            MessageUtil.sendMessage(instance, sender, ChatColor.RED + "That world is already added to the list!");
        }
    }

    @Subcommand("removeworld")
    @CommandCompletion("@worlds")
    @CommandPermission("rtp.admin.removeworld")
    public void removeWorld(CommandSender sender, World world) {
        RandomWorld randomWorld = WorldUtil.toRandomWorld(world);
        if (configWorld.contains(randomWorld)) {
            if (configWorld.remove(randomWorld)) {
                MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Removed world from the config and queue!");
            } else {
                MessageUtil.sendMessage(instance, sender, ChatColor.RED + "Something went wrong with removing the world! Is it already removed?");
            }
        } else {
            MessageUtil.sendMessage(instance, sender, ChatColor.RED + "That world is not not in the config!");
        }
    }

    @Subcommand("resetcooldown")
    @CommandCompletion("@players")
    @CommandPermission("rtp.admin.resetcooldown")
    public void resetCooldown(CommandSender sender, OnlinePlayer target) {
        if (target != null) {
            Player player = target.getPlayer();
            if (instance.getCooldowns().remove(player.getUniqueId()) != null) {
                MessageUtil.sendMessage(instance, sender, ChatColor.GREEN + "Cooldown reset for " + player.getName());
            } else {
                MessageUtil.sendMessage(instance, sender, ChatColor.RED + "There was no cooldown for " + player.getName());
            }
        }
    }

    @Subcommand("setprice")
    @CommandCompletion("<price>")
    @CommandPermission("rtp.admin.setprice")
    public void setPrice(CommandSender sender, double price) {
        if (price >= 0) {
            instance.getConfigHandler().setTeleportPrice(price);
        } else {
            MessageUtil.sendMessage(instance, sender, ChatColor.RED + "Only positive numbers are allowed.");
        }
    }

    /*@Subcommand("createSign")
    @CommandPermission("rtp.admin.createsign")
    public void createSign(Player player, World world) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        BlockFace blockFace = targetBlock.getFace(adjacentBlock);
        if (blockFace == null) {
            MessageUtil.sendMessage(plugin, player, "You done fucked up boi");
            return;
        }
        Material material;
        Location location = targetBlock.getRelative(blockFace).getLocation();
        Block signBlock = location.getBlock();
        if (blockFace == BlockFace.UP) {
            material = Material.SPRUCE_SIGN;
            location.getBlock().setType(material);
            Rotatable rotatable = (Rotatable) signBlock.getBlockData();
            rotatable.setRotation(player.getFacing().getOppositeFace());
        } else {
            material = Material.SPRUCE_WALL_SIGN;
            location.getBlock().setType(material);
            Directional directional = (Directional) signBlock.getBlockData();
            directional.setFacing(blockFace);
        }
        Sign sign = (Sign) signBlock.getState();
        List<Component> components = configMessage.getSubSectionSign().getComponents(WorldUtil.toRandomWorld(world));
        //TODO get someone else to implement components on signs
        for (int i = 0; i < components.size(); i++) {
            if (i > 4) break;
            sign.setLine(i, MiniMessage.get().serialize(components.get(i)));
        }
        sign.update();
    }*/
}
