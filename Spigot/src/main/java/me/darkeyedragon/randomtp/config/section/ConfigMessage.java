package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionSign;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.util.CustomTime;
import me.darkeyedragon.randomtp.common.util.TimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ConfigMessage implements SectionMessage {

    private String init;
    private String teleport;
    private String initTeleportDelay;
    private String teleportCanceled;
    private String depletedQueue;
    private String countdown;
    private String noWorldPermission;
    private final Economy economy;
    private final Sign sign;
    private String emptyQueue;
    private String invalidDefaultWorld;


    public ConfigMessage() {
        this.economy = new Economy();
        this.sign = new Sign();
    }

    public ConfigMessage init(String init) {
        this.init = init;
        return this;
    }

    public ConfigMessage initTeleportDelay(String initTeleportDelay) {
        this.initTeleportDelay = initTeleportDelay;
        return this;
    }

    public ConfigMessage teleportCanceled(String teleportCanceled) {
        this.teleportCanceled = teleportCanceled;
        return this;
    }

    public ConfigMessage teleport(String teleport) {
        this.teleport = teleport;
        return this;
    }

    public ConfigMessage depletedQueue(String depletedQueue) {
        this.depletedQueue = depletedQueue;
        return this;
    }

    public ConfigMessage countdown(String countdown) {
        this.countdown = countdown;
        return this;
    }

    public ConfigMessage noWorldPermission(String noWorldPermission) {
        this.noWorldPermission = noWorldPermission;
        return this;
    }

    public ConfigMessage emptyQueue(String emptyQueue) {
        this.emptyQueue = emptyQueue;
        return this;
    }

    public ConfigMessage invalidDefaultWorld(String invalidDefaultWorld){
        this.invalidDefaultWorld = invalidDefaultWorld;
        return this;
    }

    public Economy getEconomy() {
        return economy;
    }

    public SubSectionSign getSign() {
        return sign;
    }

    @Override
    public Component getInitTeleport() {
        String message = init;
        if (message != null) {
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    @Override
    public Component getTeleport(RandomLocation location) {
        String message = teleport;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = message.replaceAll("%posX", location.getX() + "")
                    .replaceAll("%posY", location.getY() + "")
                    .replaceAll("%posZ", location.getZ() + "");
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    @Override
    public Component getInitTeleportDelay(long millis) {
        String message = initTeleportDelay;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime time = TimeUtil.formatTime(millis * 50);
            message = TimeUtil.toFormattedString(message, time);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    public Component getTeleportCanceled() {
        String message = teleportCanceled;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    public Component getDepletedQueue() {
        String message = depletedQueue;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    public Component getCountdown(long remainingTime) {
        String message = countdown;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime duration = TimeUtil.formatTime(remainingTime);
            message = TimeUtil.toFormattedString(message, duration);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    @Override
    public Component getNoWorldPermission(RandomWorld world) {
        String message = noWorldPermission;
        if (message != null) {
            message = message.replace("%world", world.getName());
            message = ChatColor.translateAlternateColorCodes('&', message);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    public Component getEmptyQueue() {
        String message = emptyQueue;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return MiniMessage.get().parse(message);
        }
        return null;
    }

    @Override
    public SubSectionEconomy getSubSectionEconomy() {
        return this.economy;
    }

    @Override
    public SubSectionSign getSubSectionSign() {
        return this.sign;
    }

    @Override
    public Component getInvalidDefaultWorld(String worldName) {
        String message = invalidDefaultWorld;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = message.replace("%world", worldName);
            return MiniMessage.get().parse(message);
        }
        return null;
    }


    public static class Economy implements SubSectionEconomy {

        private String insufficientFunds;
        private String payment;

        public Economy insufficientFunds(String insufficientFunds) {
            this.insufficientFunds = insufficientFunds;
            return this;
        }

        public Economy payment(String payment) {
            this.payment = payment;
            return this;
        }

        public Component getInsufficientFunds() {
            String message = insufficientFunds;
            if (message != null) {
                message = ChatColor.translateAlternateColorCodes('&', message);
                return MiniMessage.get().parse(message);
            }
            return null;
        }

        public Component getPayment() {
            String message = payment;
            if (message != null) {
                message = ChatColor.translateAlternateColorCodes('&', message);
                return MiniMessage.get().parse(message);
            }
            return null;
        }
    }

    private static class Sign implements SubSectionSign {

        private final List<String> list;
        private List<Component> componentList;

        public Sign() {
            list = new ArrayList<>(4);
            componentList = new ArrayList<>(4);
        }

        @Override
        public List<Component> getComponents(RandomWorld world) {
            if (componentList.isEmpty()) {
                for (String item : list) {
                    String message = item;
                    message = message.replace("%world", world.getName());
                    componentList.add(MiniMessage.get().parse(message));
                }
            }
            return componentList;
        }

        @Override
        public Sign setComponents(List<String> list) {
            if (list.isEmpty()) return this;
            this.list.clear();
            this.componentList.clear();
            for (String item : list) {
                String newItem = ChatColor.translateAlternateColorCodes('&', item);
                this.list.add(newItem);
            }
            return this;
        }
    }
}
