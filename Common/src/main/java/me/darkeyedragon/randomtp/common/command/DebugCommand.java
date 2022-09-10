package me.darkeyedragon.randomtp.common.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSection;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@CommandAlias("rtpdebug")
public class DebugCommand extends BaseCommand {

    private final RandomTeleportPlugin<?> plugin;
    private final RandomConfigHandler configHandler;
    Style indexStyle = Style.style(TextColor.color(0xFFBA00));
    Style errorStyle = Style.style(TextColor.color(0xB60A00));

    public DebugCommand(RandomTeleportPlugin<?> plugin) {
        this.plugin = plugin;
        configHandler = plugin.getConfigHandler();
    }

    @Subcommand("show config")
    @CommandPermission("rtp.debug.show.config")
    public void showConfigMessages(CommandIssuer sender) {
        plugin.getMessageHandler().sendMessage(sender, Component.text("=============== [ Config Messages ] ==============").style(Style.style(TextColor.color(0x2DABBB))));
        Method[] declaredMethods = configHandler.getSectionMessage().getClass().getDeclaredMethods();
        try {
            int index = 1;
            for (Component component : methodsToComponents(configHandler.getSectionMessage(), declaredMethods)) {
                Component indexComp = Component.text(index++ + ". ").style(indexStyle);
                plugin.getMessageHandler().sendMessage(sender, indexComp.append(component));
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        plugin.getMessageHandler().sendMessage(sender, Component.text("==================================================").style(Style.style(TextColor.color(0x2DABBB))));
    }

    private List<Component> methodsToComponents(Object object, Method[] declaredMethods) throws InvocationTargetException, IllegalAccessException {
        List<Component> components = new ArrayList<>(declaredMethods.length);
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod != null && Modifier.isPublic(declaredMethod.getModifiers())) {
                Object result = instantiateObject(object, declaredMethod);
                if (result instanceof Component) {
                    Component hoverMessage = Component.text("Class: ")
                            .color(TextColor.color(0x2DABBB)
                            )
                            .append(Component.text(object.getClass().getSimpleName() + "\n")
                                    .color(TextColor.color(0x1DC700))
                            )
                            .append(Component.text("Method: ")
                                    .color(TextColor.color(0x2DABBB))
                                    .append(Component.text(declaredMethod.getName())
                                            .color(TextColor.color(0x1DC700))
                                    )
                            );
                    components.add(((Component) result).hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessage)));
                } else if (result instanceof SubSection) {
                    Method[] methods = result.getClass().getDeclaredMethods();
                    components.addAll(methodsToComponents(result, methods));
                } else {
                    if (result == null) {
                        components.add(Component.text(declaredMethod.getName() + " is null. Possibly not implemented?").style(errorStyle));
                    } else {
                        components.add(Component.text(declaredMethod.getName() + " returns " + result.getClass().getSimpleName() + ". Accepted types are Component or SubSection").style(errorStyle));
                    }
                }
            }
        }
        return components;
    }

    private Object instantiateObject(Object object, Method declaredMethod) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
        Set<Object> parameters = parameterTypeToValue(parameterTypes);
        return declaredMethod.invoke(object, parameters.toArray());
    }

    private Set<Object> parameterTypeToValue(Class<?>[] parameterTypes) {
        Set<Object> args = new HashSet<>();
        for (Class<?> type : parameterTypes) {
            if (type.isPrimitive()) {
                args.add(100000);
            } else if (type.isAssignableFrom(String.class)) {
                args.add("<String>");
            } else if (type.isAssignableFrom(RandomWorld.class)) {
                //Grab the first world from the world queue. If non exists... well then it dies.
                Set<RandomWorld> worldQueue = plugin.getWorldHandler().getWorldQueue().getWorldQueueMap().keySet();
                Iterator<RandomWorld> worldIterator = worldQueue.iterator();
                args.add(worldIterator.next());
            } else if (type.isAssignableFrom(RandomLocation.class)) {
                Set<RandomWorld> worldQueue = plugin.getWorldHandler().getWorldQueue().getWorldQueueMap().keySet();
                Iterator<RandomWorld> worldIterator = worldQueue.iterator();
                args.add(plugin.getWorldHandler().getWorld(worldIterator.next().getName()).getBlockAt(150, 10, 200).getLocation());
            }
        }
        return args;
    }

    @Subcommand("show queue")
    @CommandPermission("rtp.debug.show.queue")
    public void showQueue(CommandIssuer sender) {
        WorldQueue worldQueue = plugin.getWorldHandler().getWorldQueue();
        Component component = ComponentUtil.toComponent("<aqua>=============== [ Queue ] ==============");
        for (RandomWorld world : worldQueue.getWorldQueueMap().keySet()) {
            component = component.append(Component.text("\n" + world.getName()));
            LocationQueue locationQueue = worldQueue.get(world);
            RandomLocation[] locations = locationQueue.toArray(new RandomLocation[0]);
            for (int i = 0; i < configHandler.getSectionQueue().getSize(); i++) {
                if (locations.length > i) {
                    RandomLocation randomLocation = locations[i];
                    component = component
                            .append(ComponentUtil.toComponent("\n     \u2514<green>" + randomLocation.getBlockX() + "x " + randomLocation.getBlockY() + "y " + randomLocation.getBlockZ() + "z"))
                            .append(ComponentUtil.toComponent(" <gold>[" + randomLocation.getBlock().getBiome().getName() + "]"));
                } else {
                    component = component.append(Component.text("\n     \u2514")).append(Component.text("Pending location...").style(Style.style(TextColor.color(0xff0000))));
                }
            }
        }
        component = component.append(ComponentUtil.toComponent("\n<aqua>======================================"));
        plugin.getMessageHandler().sendMessage(sender, component);
    }

}
