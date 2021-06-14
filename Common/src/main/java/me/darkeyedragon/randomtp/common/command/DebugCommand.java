package me.darkeyedragon.randomtp.common.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@CommandAlias("rtpdebug")
public class DebugCommand extends BaseCommand {

    private final RandomTeleportPlugin<?> plugin;
    private final RandomConfigHandler configHandler;

    public DebugCommand(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        configHandler = plugin.getConfigHandler();
    }

    @Subcommand("show config")
    @CommandPermission("rtp.debug.show.config")
    public void showConfigMessages(CommandIssuer sender) {
        plugin.getMessageHandler().sendMessage(sender, Component.text("=============== [ Config Messages ] ==============").style(Style.style(TextColor.color(0x2DABBB))));
        Method[] declaredMethods = configHandler.getSectionMessage().getClass().getDeclaredMethods();
        int index = 0;
        for (Method declaredMethod : declaredMethods) {
            if (Modifier.isPublic(declaredMethod.getModifiers())) {
                index++;
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                List<Object> args = new ArrayList<>();
                for (Class<?> type : parameterTypes) {
                    System.out.println(type.getTypeName());
                    if (type.isPrimitive()) {
                        args.add(100000);
                    } else if (type.isAssignableFrom(String.class)) {
                        args.add("testStr");
                    } else if (type.isAssignableFrom(RandomWorld.class)) {
                        //Grab the first world from the world queue. If non exists... well then it dies.
                        Set<RandomWorld> worldQueue = plugin.getWorldHandler().getWorldQueue().getWorldQueueMap().keySet();
                        Iterator<RandomWorld> worldIterator = worldQueue.iterator();
                        args.add(worldIterator.next());
                    } else if (type.isAssignableFrom(RandomLocation.class)) {
                        args.add(plugin.getWorldHandler().getWorld("world").getBlockAt(150, 10, 200).getLocation());
                    }
                }
                try {
                    Object obj;
                    if (args.size() > 0) {
                        obj = declaredMethod.invoke(configHandler.getSectionMessage(), args.toArray());
                    } else {
                        obj = declaredMethod.invoke(configHandler.getSectionMessage());
                    }
                    Component component;
                    Component indicator = Component.text(index);
                    indicator = indicator.style(Style.style(TextColor.color(0xAAAAAA))).append(Component.text(". "));
                    if (obj == null) {
                        Component finalText = indicator.append(Component.text(declaredMethod.getName())).append(Component.text(" is null!").hoverEvent(Component.text(declaredMethod.getName())));
                        plugin.getMessageHandler().sendMessage(sender, finalText);
                    } else {
                        component = (Component) obj;
                        Component finalText = indicator.append(component).hoverEvent(Component.text(declaredMethod.getName()));
                        plugin.getMessageHandler().sendMessage(sender, finalText);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subcommand("show queue")
    @CommandPermission("rtp.debug.show.queue")
    public void showQueue(CommandIssuer sender) {
        WorldQueue worldQueue = plugin.getWorldHandler().getWorldQueue();
        Component component = Component.text("=============== [ Queue ] ==============");
        for (RandomWorld world : worldQueue.getWorldQueueMap().keySet()) {
            component = component.append(Component.text("\n" + world.getName()));
            LocationQueue locationQueue = worldQueue.get(world);
            RandomLocation[] locations = locationQueue.toArray(new RandomLocation[0]);
            for (int i = 0; i < configHandler.getSectionQueue().getSize(); i++) {
                if (locations.length > i) {
                    RandomLocation randomLocation = locations[i];
                    component = component.append(Component.text("\n         \u2514")).append(Component.text(randomLocation.getBlockX() + "x " + randomLocation.getBlockY() + "y " + randomLocation.getBlockZ() + "z").style(Style.style(TextColor.color(0x00ff00))));
                } else {
                    component = component.append(Component.text("\n         \u2514")).append(Component.text("Pending location...").style(Style.style(TextColor.color(0xff0000))));
                }
            }
        }
        plugin.getMessageHandler().sendMessage(sender, component);
    }

}
