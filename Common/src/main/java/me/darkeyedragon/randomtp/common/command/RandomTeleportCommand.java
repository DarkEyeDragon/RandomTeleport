package me.darkeyedragon.randomtp.common.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.randomtp.api.addon.RandomAddon;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.QueueListener;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.RandomParticle;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.world.RandomPlayer;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.Offset;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.teleport.BasicTeleportHandler;
import me.darkeyedragon.randomtp.common.teleport.CommonTeleportProperty;
import me.darkeyedragon.randomtp.common.util.TimeUtil;
import me.darkeyedragon.randomtp.common.world.WorldConfigSection;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("rtp|randomtp|randomteleport")
public class RandomTeleportCommand extends BaseCommand {

    private final MessageHandler messageHandler;
    private final RandomTeleportPlugin<?> plugin;
    //private BaseLocationSearcher locationHelper;
    private final CooldownHandler cooldownHandler;
    private RandomConfigHandler configHandler;
    private WorldQueue worldQueue;

    //Config sections
    private SectionMessage configMessage;
    private SectionQueue configQueue;
    private SectionWorld configWorld;
    private SectionTeleport configTeleport;

    public RandomTeleportCommand(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        messageHandler = plugin.getMessageHandler();
        cooldownHandler = plugin.getCooldownHandler();
        setConfigs();
    }

    private void setConfigs() {
        this.configHandler = plugin.getConfigHandler();
        this.configMessage = configHandler.getSectionMessage();
        this.configQueue = configHandler.getSectionQueue();
        this.configWorld = configHandler.getSectionWorld();
        this.worldQueue = plugin.getWorldHandler().getWorldQueue();
        this.configTeleport = configHandler.getSectionTeleport();
    }

    @Default
    @CommandPermission("rtp.teleport.self")
    @CommandCompletion("@playerWorlds")
    @Description("Teleport players to a random location.")
    @Syntax("[world/player] [world]")
    public void onTeleport(CommandIssuer sender, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") RandomWorld world) {
        RandomPlayer player = null;
        List<RandomPlayer> targets = new ArrayList<>();
        RandomWorld newWorld;
        if (target == null) {
            if (sender.isPlayer()) {
                player = plugin.getPlayerHandler().getPlayer(sender.getUniqueId());
                if (configTeleport.getUseDefaultWorld()) {
                    newWorld = plugin.getWorldHandler().getWorld(world.getName());
                    if (newWorld == null) {
                        plugin.getMessageHandler().sendMessage(player, configMessage.getInvalidDefaultWorld(configTeleport.getDefaultWorld()));
                        return;
                    }
                    //Check if world is in the queue, if not, well then there are no locations, go figure.
                    LocationQueue locationQueue = plugin.getWorldHandler().getWorldQueue().get(world);
                    if (locationQueue == null) {
                        plugin.getMessageHandler().sendMessage(player, configMessage.getInvalidDefaultWorld(configTeleport.getDefaultWorld()));
                        return;
                    }
                } else {
                    newWorld = plugin.getWorldHandler().getWorld(player.getWorld().getName());
                }
                if (!configWorld.contains(newWorld)) {
                    plugin.getMessageHandler().sendMessage(sender, configMessage.getNoWorldPermission(newWorld));
                    return;
                }
            } else {
                throw new InvalidCommandArgument(true);
            }
        } else {
            if (target.getPlayers().size() > 0) {
                if (sender.hasPermission("rtp.teleport.other")) {
                    targets = target.getPlayers();
                    //If no world is provided check if the default world is enabled
                    if (configTeleport.getUseDefaultWorld()) {
                        newWorld = plugin.getWorldHandler().getWorld(configTeleport.getDefaultWorld());
                    } else {
                        newWorld = world;
                    }
                    if (!configWorld.contains(newWorld)) {
                        if (newWorld == null) {

                            throw new InvalidCommandArgument(true);
                        }
                        plugin.getMessageHandler().sendMessage(sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                } else {
                    plugin.getMessageHandler().sendMessage(sender, "<red>I'm sorry, you do not have permission to perform this command!");
                    return;
                }
            } else if (target.isWorld()) {
                if (sender.isPlayer()) {
                    player = plugin.getPlayerHandler().getPlayer(sender.getUniqueId());
                    newWorld = target.getWorld();
                    if (!configWorld.contains(newWorld)) {
                        plugin.getMessageHandler().sendMessage(sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    SectionWorldDetail sectionWorldDetail = plugin.getConfigHandler().getSectionWorld().getSectionWorldDetail(newWorld);
                    if (sectionWorldDetail == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && sectionWorldDetail.needsWorldPermission())) {
                        plugin.getMessageHandler().sendMessage(sender, configMessage.getNoWorldPermission(newWorld));
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
        if (player == null) {
            for (RandomPlayer target1 : targets) {
                teleport(sender, target1, finalWorld);
            }
        } else {
            teleport(sender, player, finalWorld);
        }
    }

    private void teleport(CommandIssuer sender, RandomPlayer player, RandomWorld world) {
        final boolean useEco = configHandler.getSectionEconomy().useEco();
        boolean bypassDelay = player.hasPermission("rtp.teleportdelay.bypass") || sender.hasPermission("rtp.teleportdelay.bypass");
        boolean bypasCooldown = player.hasPermission("rtp.teleport.bypass") || sender.hasPermission("rtp.teleport.bypass");
        boolean bypassEco = useEco && (player.hasPermission("rtp.eco.bypass") || sender.hasPermission("rtp.eco.bypass"));
        RandomLocation location = worldQueue.popLocation(world);
        TeleportProperty teleportProperty = new CommonTeleportProperty(location, sender, player, bypassEco, bypassDelay, bypasCooldown, configTeleport.getParticle());
        BasicTeleportHandler teleportHandler = new BasicTeleportHandler(plugin, teleportProperty);
        teleportHandler.toRandomLocation(player);
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    public void onReload(CommandIssuer sender) {
        sender.sendMessage("<green>Reloading config...");
        plugin.getConfigHandler().saveConfig();
        plugin.reloadConfig();
        //Set the new config object references
        setConfigs();
        messageHandler.sendMessage(sender, "<green>Clearing queue...");
        worldQueue.clear();
        messageHandler.sendMessage(sender, "<green>Repopulating queue, this can take a while.");
        plugin.getWorldHandler().populateWorldQueue();
        messageHandler.sendMessage(sender, "<green>Reloaded config");
    }

    @Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @Syntax("<world> <useWorldBorder> <needsWorldPermission> [radius] [offsetX] [offsetZ]")
    @CommandCompletion("@worlds true|false true|false")
    public void onAddWorld(CommandIssuer sender, RandomWorld randomWorld, boolean useWorldBorder, boolean needsWorldPermission, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            messageHandler.sendMessage(sender, "<gold>If <aqua>useWorldBorder<gold> is false you need to provide the other parameters.");
            throw new InvalidCommandArgument(true);
        }
        if (!configWorld.contains(randomWorld)) {
            if (useWorldBorder) {
                if (radius == null) radius = 0;
                if (offsetX == null) offsetX = 0;
                if (offsetZ == null) offsetZ = 0;
            }
            configWorld.add(new WorldConfigSection(new Offset(offsetX, offsetZ, radius), randomWorld, useWorldBorder, needsWorldPermission));
            LocationQueue locationQueue = worldQueue.get(randomWorld);
            if (locationQueue != null) {
                messageHandler.sendMessage(sender, "<green>Successfully added to config.");
                locationQueue.subscribe(new QueueListener<RandomLocation>() {
                    @Override
                    public void onAdd(RandomLocation element) {
                        messageHandler.sendMessage(sender, "<green>Safe location added for <gold>" + element.getWorld().getName() + "<green> (<yellow>" + locationQueue.size() + "<green>/<yellow>" + configQueue.getSize() + ")");
                        if (locationQueue.size() == configQueue.getSize()) {
                            messageHandler.sendMessage(sender, "<green>Queue populated for <gold>" + element.getWorld().getName());
                            locationQueue.unsubscribe(this);
                        }
                    }

                    @Override
                    public void onRemove(RandomLocation element) {
                        //ignored
                    }
                });
            } else {
                messageHandler.sendMessage(sender, "<red>Size section not present in the config! Add it or recreate your config.");
            }
        } else {
            messageHandler.sendMessage(sender, "<red>That world is already added to the list!");
        }
    }

    @Subcommand("removeworld")
    @CommandCompletion("@worlds")
    @CommandPermission("rtp.admin.removeworld")
    public void removeWorld(CommandIssuer sender, RandomWorld world) {

        if (configWorld.contains(world)) {
            if (configWorld.remove(world)) {
                messageHandler.sendMessage(sender, "<green>Removed world from the config and queue!");
            } else {
                messageHandler.sendMessage(sender, "<red>Something went wrong with removing the world! Is it already removed?");
            }
        } else {
            messageHandler.sendMessage(sender, "<red>That world is not not in the config!");
        }
    }

    @Subcommand("resetcooldown")
    @CommandCompletion("@players")
    @CommandPermission("rtp.admin.resetcooldown")
    public void resetCooldown(CommandIssuer sender, RandomPlayer target) {
        if (target != null) {
            if (plugin.getCooldownHandler().removeCooldown(target.getUniqueId()) != null) {
                messageHandler.sendMessage(sender, "<green>RandomCooldown reset for " + target.getName());
            } else {
                messageHandler.sendMessage(sender, "<red>There was no cooldown for " + target.getName());
            }
        }
    }

    @Subcommand("setprice")
    @CommandCompletion("<price>")
    @CommandPermission("rtp.admin.setprice")
    public void setPrice(CommandIssuer sender, double price) {
        if (price >= 0) {
            configHandler.getSectionEconomy().setPrice(price);
        } else {
            messageHandler.sendMessage(sender, "<red>Only positive numbers are allowed.");
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

    @Subcommand("addon")
    public class AddonClass extends BaseCommand {

        @Subcommand("register")
        @CommandCompletion("@addonFiles")
        @CommandPermission("rtp.admin.addon.register")
        public void register(CommandIssuer sender, String name) {
            RandomAddon addon = plugin.getAddonManager().register(name);
            if (addon != null) {
                messageHandler.sendMessage(sender, "<green>" + addon.getIdentifier() + " has been registered.");
            } else {
                messageHandler.sendMessage(sender, "<red> could not be registered. Is the name correct?");
            }
        }

        @Subcommand("unregister")
        @CommandCompletion("@addonNames")
        @CommandPermission("rtp.admin.addon.unregister")
        public void unregister(CommandIssuer sender, String name) {
            RandomLocationValidator addon = plugin.getAddonManager().unregister(name);
            if (addon != null) {
                messageHandler.sendMessage(sender, "<green>" + addon.getIdentifier() + " has been unregistered.");
            } else {
                messageHandler.sendMessage(sender, "<red> could not be unregistered. Is it loaded?");
            }

        }

        @Subcommand("list")
        @CommandPermission("rtp.admin.addon.list")
        public void list(CommandIssuer sender) {
            StringBuilder message = new StringBuilder("<aqua>========= [Addons] ========").append("\n");
            for (RandomAddon addon : plugin.getAddonManager().getAddons().values()) {
                message.append("<gold>").append(addon.getIdentifier()).append("\n");
                for (RequiredPlugin requiredPlugin : addon.getRequiredPlugins()) {
                    message.append("<green>").append("  └─ ").append(requiredPlugin.getName()).append("\n");
                }
            }
            message.append("<aqua>").append("\n").append("=========================");
            messageHandler.sendMessage(sender, message.toString());
        }
    }

    @Subcommand("teleport")
    public class TeleportSettingsCommand {

        @Subcommand("setcooldown")
        @CommandPermission("rtp.admin.cooldown")
        public void setCooldown(CommandIssuer sender, String time) {
            try {
                long cooldown = TimeUtil.stringToLong(time);
                configTeleport.setCooldown(cooldown);
            } catch (NumberFormatException ex) {
                sender.sendMessage("<red>Unable to parse time: " + ex.getMessage());
            }
        }

        @Subcommand("setdelay")
        @CommandPermission("rtp.admin.delay")
        public void setDelay(CommandIssuer sender, String time) {
            try {
                long delay = TimeUtil.stringToLong(time);
                configTeleport.setCooldown(delay);
                sendConfigSuccessMessage(sender);
            } catch (NumberFormatException ex) {
                sender.sendMessage("<red>Unable to parse time: " + ex.getMessage());
            }
        }

        @Subcommand("setcancelonmove")
        @CommandPermission("rtp.admin.cancel_on_move")
        public void setCancelOnMove(CommandIssuer sender, boolean cancelOnMove) {
            configTeleport.setCancelOnMove(cancelOnMove);
            sendConfigSuccessMessage(sender);
        }

        @Subcommand("setparticle")
        @CommandPermission("rtp.admin.particle")
        @CommandCompletion("@particles")
        public void setParticle(CommandIssuer sender, String particleType, int amount) {
            //TODO implement
            RandomParticle<?> particle = null;
            configTeleport.setParticle(particle);
            sendConfigSuccessMessage(sender);
        }

        @Subcommand("setusedefaultworld")
        @CommandPermission("rtp.admin.use_default_world")
        public void setUseDefaultWorld(CommandIssuer sender, boolean useDefaultWorld) {
            configTeleport.setUseDefaultWorld(useDefaultWorld);
            sendConfigSuccessMessage(sender);
        }

        @Subcommand("setdefaultworld")
        @CommandPermission("rtp.admin.default_world")
        @CommandCompletion("@worlds")
        public void setDefaultWorld(CommandIssuer sender, String world) {
            configTeleport.setDefaultWorld(world);
            sendConfigSuccessMessage(sender);
        }

        private void sendConfigSuccessMessage(CommandIssuer sender) {
            sender.sendMessage("<green>Configuration has been updated.");
        }
    }

}
