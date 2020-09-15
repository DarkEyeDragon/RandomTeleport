package me.darkeyedragon.randomtp.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.config.ConfigHandler;
import me.darkeyedragon.randomtp.teleport.Teleport;
import me.darkeyedragon.randomtp.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.util.MessageUtil;
import me.darkeyedragon.randomtp.util.WorldUtil;
import me.darkeyedragon.randomtp.world.location.LocationFactory;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

@CommandAlias("rtp|randomtp|randomteleport")
public class TeleportCommand extends BaseCommand {

    //private BaseLocationSearcher locationHelper;
    private ConfigHandler configHandler;
    private final RandomTeleport plugin;
    private WorldQueue worldQueue;
    private LocationFactory locationFactory;
    private boolean teleportSuccess;

    //Economy

    //Config sections
    private SectionMessage configMessage;
    private SectionQueue configQueue;
    private SectionWorld configWorld;

    public TeleportCommand(RandomTeleport plugin) {
        this.plugin = plugin;
        setConfigs();
    }

    private void setConfigs() {
        this.configHandler = plugin.getConfigHandler();
        this.configMessage = configHandler.getSectionMessage();
        this.configQueue = configHandler.getSectionQueue();
        this.configWorld = configHandler.getSectionWorld();
        this.locationFactory = plugin.getLocationFactory();
        this.worldQueue = plugin.getWorldQueue();
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@players|@permWorlds")
    public void onTeleport(CommandSender sender, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") World world) {
        Player player;
        RandomWorld newWorld;
        if (target == null) {
            if (sender instanceof Player) {
                player = (Player) sender;
                newWorld = WorldUtil.toRandomWorld(player.getWorld());
                if (!configWorld.contains(newWorld)) {
                    MessageUtil.sendMessage(plugin, sender, configMessage.getNoWorldPermission(newWorld));
                    return;
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.isPlayer()) {
                if (sender.hasPermission("rtp.teleport.other")) {
                    player = target.getPlayer();
                    newWorld = WorldUtil.toRandomWorld(world);
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(plugin, sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                } else {
                    MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "I'm sorry, you do not have permission to perform this command!");
                    return;
                }
            } else if (target.isWorld()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    newWorld = target.getWorld();
                    if (!configWorld.contains(newWorld)) {
                        MessageUtil.sendMessage(plugin, sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(newWorld);
                    if (worldConfigSection == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && worldConfigSection.needsWorldPermission())) {
                        MessageUtil.sendMessage(plugin, sender, configMessage.getNoWorldPermission(newWorld));
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
        final boolean useEco = configHandler.getSectionEconomy().useEco();
        final boolean bypassEco = player.hasPermission("rtp.eco.bypass");
        final boolean logic = useEco && !bypassEco;
        TeleportProperty teleportProperty = new TeleportProperty(sender, player, finalWorld, sender.hasPermission("rtp.teleport.bypass"), sender.hasPermission("rtp.teleportdelay.bypass"), logic, configHandler, configHandler.getSectionTeleport().getCooldown());
        Teleport teleport = new Teleport(plugin, teleportProperty);
        teleport.random();
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Reloading config...");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        try {
            plugin.getConfigHandler().reload();
        } catch (InvalidConfigurationException e) {
            MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "Your config is not configured properly. Error:");
            MessageUtil.sendMessage(plugin, sender, ChatColor.RED + e.getMessage());
            e.printStackTrace();
        }
        //Set the new config object references
        setConfigs();
        MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Clearing queue...");
        worldQueue.clear();
        MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Repopulating queue, this can take a while.");
        plugin.populateWorldQueue();
        MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @CommandCompletion("@worlds true|false true|false <Integer> <Integer> <Integer>")
    public void onAddWorld(CommandSender sender, World world, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        RandomWorld randomWorld = WorldUtil.toRandomWorld(world);
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            MessageUtil.sendMessage(plugin, sender, ChatColor.GOLD + "If " + ChatColor.AQUA + "useWorldBorder" + ChatColor.GOLD + " is false you need to provide the other parameters.");
            throw new InvalidCommandArgument(true);
        }
        if (!configWorld.contains(randomWorld)) {
            if (useWorldBorder) {
                if (radius == null) radius = 0;
                if (offsetX == null) offsetX = 0;
                if (offsetZ == null) offsetZ = 0;
            }
            //WorldUtil.WORLD_MAP.put(Bukkit.getWorld(randomWorld.getUUID()), randomWorld);
            configWorld.add(new WorldConfigSection(offsetX, offsetZ, radius, randomWorld, useWorldBorder, needsWorldPermission));
            LocationQueue locationQueue = plugin.getQueue(randomWorld);
            if (locationQueue != null) {
                MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Successfully added to config.");
                locationQueue.subscribe(new QueueListener<RandomLocation>() {
                    @Override
                    public void onAdd(RandomLocation element) {
                        MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Safe location added for " + ChatColor.GOLD + element.getWorld().getName() + ChatColor.GREEN + " (" + ChatColor.YELLOW + locationQueue.size() + ChatColor.GREEN + "/" + configQueue.getSize() + ")");
                        if (locationQueue.size() == configQueue.getSize()) {
                            MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Queue populated for " + ChatColor.GOLD + element.getWorld().getName());
                            locationQueue.unsubscribe(this);
                        }
                    }

                    @Override
                    public void onRemove(RandomLocation element) {
                        //ignored
                    }
                });
            } else {
                MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "That world is already added to the list!");
        }
    }

    @Subcommand("removeworld")
    @CommandCompletion("@worlds")
    @CommandPermission("rtp.admin.removeworld")
    public void removeWorld(CommandSender sender, World world) {
        RandomWorld randomWorld = WorldUtil.toRandomWorld(world);
        if (configWorld.contains(randomWorld)) {
            if (configWorld.remove(randomWorld)) {
                MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Removed world from the config and queue!");
            } else {
                MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "Something went wrong with removing the world! Is it already removed?");
            }
        } else {
            MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "That world is not not in the config!");
        }
    }

    @Subcommand("resetcooldown")
    @CommandCompletion("@players")
    @CommandPermission("rtp.admin.resetcooldown")
    public void resetCooldown(CommandSender sender, OnlinePlayer target) {
        if (target != null) {
            Player player = target.getPlayer();
            if (plugin.getCooldowns().remove(player.getUniqueId()) != null) {
                MessageUtil.sendMessage(plugin, sender, ChatColor.GREEN + "Cooldown reset for " + player.getName());
            } else {
                MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "There was no cooldown for " + player.getName());
            }
        }
    }

    @Subcommand("setprice")
    @CommandCompletion("<price>")
    @CommandPermission("rtp.admin.setprice")
    public void setPrice(CommandSender sender, double price) {
        if (price >= 0) {
            plugin.getConfigHandler().setTeleportPrice(price);
        } else {
            MessageUtil.sendMessage(plugin, sender, ChatColor.RED + "Only positive numbers are allowed.");
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
