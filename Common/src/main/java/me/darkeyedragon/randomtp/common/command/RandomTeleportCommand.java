package me.darkeyedragon.randomtp.common.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.datatype.ConfigWorld;
import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.message.MessageHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.teleport.CooldownHandler;
import me.darkeyedragon.randomtp.api.teleport.RandomCooldown;
import me.darkeyedragon.randomtp.api.teleport.TeleportProperty;
import me.darkeyedragon.randomtp.api.teleport.TeleportResponse;
import me.darkeyedragon.randomtp.api.teleport.TeleportType;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.player.RandomPlayer;
import me.darkeyedragon.randomtp.common.command.context.PlayerWorldContext;
import me.darkeyedragon.randomtp.common.teleport.BasicTeleportHandler;
import me.darkeyedragon.randomtp.common.teleport.BasicTeleportResponse;
import me.darkeyedragon.randomtp.common.teleport.CommonTeleportPropertyBuilder;

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
    private long timeSpan;
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

    //Call this before commands that rely on config values to make sure they are updated with their latest values
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
    @CommandCompletion("@playerFilteredWorlds @filteredWorlds")
    @Description("Teleport players to a random location.")
    @Syntax("[world/player] [world]")
    public void onTeleport(CommandIssuer sender, @Optional PlayerWorldContext target, @Optional @CommandPermission("rtp.teleport.world") RandomWorld world) {
        if (plugin.getConfigHandler().getSectionDebug().isShowExecutionTimes()) {
            timeSpan = System.currentTimeMillis();
        }
        setConfigs();
        RandomPlayer player = null;
        List<RandomPlayer> targets = new ArrayList<>();
        RandomWorld newWorld;
        if (target == null) {
            if (sender.isPlayer()) {
                player = plugin.getPlayerHandler().getPlayer(sender.getUniqueId());
                if (configTeleport.getUseDefaultWorld()) {
                    newWorld = plugin.getWorldHandler().getWorld(configTeleport.getDefaultWorld());
                    if (newWorld == null) {
                        plugin.getMessageHandler().sendMessage(player, configMessage.getInvalidDefaultWorld(configTeleport.getDefaultWorld()));
                        return;
                    }
                    //Check if world is in the queue, if not, well then there are no locations, go figure.
                    LocationQueue locationQueue = plugin.getWorldHandler().getWorldQueue().get(newWorld);
                    if (locationQueue == null) {
                        plugin.getMessageHandler().sendMessage(player, configMessage.getInvalidDefaultWorld(configTeleport.getDefaultWorld()));
                        return;
                    }
                } else {
                    newWorld = plugin.getWorldHandler().getWorld(player.getWorld().getName());
                }
                if (!configWorld.contains(newWorld.getName())) {
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
                    if (newWorld == null) {
                        throw new InvalidCommandArgument("World with that name does not exist!");
                    }
                    if (!configWorld.contains(newWorld.getName())) {
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
                    if (!configWorld.contains(newWorld.getName())) {
                        plugin.getMessageHandler().sendMessage(sender, configMessage.getNoWorldPermission(newWorld));
                        return;
                    }
                    ConfigWorld worldDetail = plugin.getConfigHandler().getSectionWorld().getConfigWorld(newWorld.getName());
                    if (worldDetail == null || ((!sender.hasPermission("rtp.world." + newWorld.getName())) && worldDetail.isNeedsWorldPermission())) {
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

    private TeleportResponse teleport(CommandIssuer sender, RandomPlayer player, RandomWorld world) {
        setConfigs();
        final ConfigWorld worldDetail = plugin.getConfigHandler().getSectionWorld().getConfigWorld(world.getName());
        double price = 0;
        switch (worldDetail.getEcoType()) {
            case GLOBAL:
                price = configHandler.getSectionEconomy().getPrice();
                break;
            case LOCAL:
                price = worldDetail.getPrice();
                break;
            case NONE:
                price = 0;
                break;
        }
        boolean bypassDelay = player.hasPermission("rtp.teleportdelay.bypass") || sender.hasPermission("rtp.teleportdelay.bypass");
        boolean bypassCooldown = player.hasPermission("rtp.teleport.bypass") || sender.hasPermission("rtp.teleport.bypass");
        boolean bypassEco = player.hasPermission("rtp.eco.bypass") || sender.hasPermission("rtp.eco.bypass");
        boolean cancelOnMove = configTeleport.isCancelOnMove();
        long delay = bypassDelay ? 0 : configHandler.getSectionTeleport().getDelay();

        CooldownHandler cooldownHandler = plugin.getCooldownHandler();
        RandomCooldown cooldown = cooldownHandler.getCooldown(player);
        if (!bypassCooldown && cooldown != null && cooldown.getRemainingTime() > 0) {
            plugin.getMessageHandler().sendMessage(player, configHandler.getSectionMessage().getCountdown(cooldown.getRemainingTime() / 50));
            return new BasicTeleportResponse(TeleportType.COOLDOWN);
        }
        TeleportProperty teleportProperty = new CommonTeleportPropertyBuilder()
                .commandIssuer(sender)
                .target(player)
                .price(price)
                .bypassEco(bypassEco)
                .bypassTeleportDelay(bypassDelay)
                .bypassCooldown(bypassCooldown)
                .particle(configTeleport.getParticle())
                .initTime(timeSpan)
                .delay(delay)
                .cancelOnMove(cancelOnMove)
                .world(world)
                .build();
        BasicTeleportHandler teleportHandler = new BasicTeleportHandler(plugin);
        TeleportResponse response = teleportHandler.toRandomLocation(teleportProperty);
        if (timeSpan != 0 && configHandler.getSectionDebug().isShowExecutionTimes()) {
            long totalTime = System.currentTimeMillis() - timeSpan;
            plugin.getLogger().info("Debug: Teleport request took: " + totalTime + "ms");
        }
        return response;
    }

    @Subcommand("reload")
    @CommandPermission("rtp.admin.reload")
    @Description("Reload the rtp config")
    public void onReload(CommandIssuer sender) {
        messageHandler.sendMessage(sender, "<green>Reloading config...");
        plugin.reloadConfig();
        //Set the new config object references
        setConfigs();
        messageHandler.sendMessage(sender, "<green>Clearing queue...");
        worldQueue.clear();
        messageHandler.sendMessage(sender, "<green>Repopulating queue, this can take a while.");
        plugin.getWorldHandler().populateWorldQueue();
        messageHandler.sendMessage(sender, "<green>Reloaded config");
    }

    /*@Subcommand("addworld")
    @CommandPermission("rtp.admin.addworld")
    @Syntax("<world> <useWorldBorder> <needsWorldPermission> [price] [radius] [offsetX] [offsetZ]")
    @CommandCompletion("@worlds true|false true|false")
    public void onAddWorld(CommandIssuer sender, RandomWorld randomWorld, boolean useWorldBorder, boolean needsWorldPermission, @Optional double price, @Optional Integer radius, @Optional Integer offsetX, @Optional Integer offsetZ) {
        setConfigs();
        if (randomWorld == null) {
            throw new InvalidCommandArgument("This world does not exist.", true);
        }
        if (!useWorldBorder && (radius == null || offsetX == null || offsetZ == null)) {
            messageHandler.sendMessage(sender, "<gold>If <aqua>useWorldBorder<gold> is false you need to provide the other parameters.");
            throw new InvalidCommandArgument(true);
        }
        if (!configWorld.contains(randomWorld.getName())) {
            if (useWorldBorder) {
                if (radius == null) radius = 0;
                if (offsetX == null) offsetX = 0;
                if (offsetZ == null) offsetZ = 0;
            }
            //TODO add to config
            /*if (configWorld.add(new WorldConfigSection(new Offset(offsetX, offsetZ, radius), randomWorld, price, useWorldBorder, needsWorldPermission))) {
                plugin.getWorldHandler().getWorldQueue().put(randomWorld, new LocationQueue(plugin, configQueue.getSize(), LocationSearcherFactory.getLocationSearcher(randomWorld, plugin)));
            }*/
            /*LocationQueue locationQueue = worldQueue.get(randomWorld);
            if (locationQueue != null) {
                messageHandler.sendMessage(sender, "<green>Successfully added to config.");
                locationQueue.subscribe(new CommonQueueListener(plugin, randomWorld, locationQueue) {
                    @Override
                    public void onAdd(RandomLocation element) {
                        messageHandler.sendMessage(sender, "<green>Safe location added for <gold>" + element.getWorld().getName() + "<green> (<yellow>" + locationQueue.size() + "<green>/<yellow>" + configQueue.getSize() + ")");
                        if (locationQueue.size() == configQueue.getSize()) {
                            messageHandler.sendMessage(sender, "<green>Queue populated for <gold>" + element.getWorld().getName());
                            locationQueue.unsubscribe(this);
                        }
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
        setConfigs();
        if (configWorld.contains(world.getName())) {
            if (configWorld.remove(world.getName())) {
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

    /*@Subcommand("addon")
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

    @Subcommand("delay")
    @CommandPermission("rtp.admin.delay")
    @Description("Set the cooldown before teleports")
    @Syntax("<time>")
    @CommandCompletion("1s|1m|1h")
    public void setDelay(CommandIssuer sender, String time) {
        try {
            long delay = TimeUtil.stringToLong(time);
            configTeleport.setCooldown(delay);
            sendConfigSuccessMessage(sender);
        } catch (NumberFormatException ex) {
            sender.sendMessage("<red>Unable to parse time: " + ex.getMessage());
        }
    }

    @Subcommand("cancelonmove")
    @CommandPermission("rtp.admin.cancel_on_move")
    public void setCancelOnMove(CommandIssuer sender, boolean cancelOnMove) {
        configTeleport.setCancelOnMove(cancelOnMove);
        sendConfigSuccessMessage(sender);
    }

    @Subcommand("setparticle")
    @CommandPermission("rtp.admin.particle")
    @CommandCompletion("@particles")
    public void setParticle(CommandIssuer sender, String particleName, int amount) {
        RandomParticle particle = new CommonParticle(particleName, amount);
        configTeleport.setParticle(particle);
        sendConfigSuccessMessage(sender);
    }

    @Subcommand("defaultworld")
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

    @Subcommand("set")
    public class SetValues {
        @Subcommand("cooldown")
        @CommandPermission("rtp.admin.cooldown")
        @Description("Set the cooldown between teleports")
        @Syntax("<time>")
        @CommandCompletion("1s|1m|1h")
        public void setCooldown(CommandIssuer sender, String time) {
            try {
                long cooldown = TimeUtil.stringToLong(time);
                configTeleport.setCooldown(cooldown);
            } catch (NumberFormatException ex) {
                plugin.getMessageHandler().sendMessage(sender, "<red>Unable to parse time: " + ex.getMessage());
            }
        }
    }*/
}
