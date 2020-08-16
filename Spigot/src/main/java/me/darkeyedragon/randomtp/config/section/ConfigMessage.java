package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionMessage;
import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.util.CustomTime;
import me.darkeyedragon.randomtp.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigMessage implements SectionMessage {

    private String init;
    private String teleport;
    private String initTeleportDelay;
    private String teleportCanceled;
    private String depletedQueue;
    private String countdown;
    private String noWorldPermission;
    private final Economy economy;
    private String emptyQueue;


    public ConfigMessage() {
        this.economy = new Economy();
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

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public String getInitTeleport() {
        String message = init;
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return null;
    }

    @Override
    public String getTeleport(RandomLocation location) {
        String message = teleport;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            message = message.replaceAll("%posX", location.getX() + "")
                    .replaceAll("%posY", location.getY() + "")
                    .replaceAll("%posZ", location.getZ() + "");
            return message;
        }
        return null;
    }

    @Override
    public String getInitTeleportDelay(long millis) {
        String message = initTeleportDelay;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime time = TimeUtil.formatTime(millis * 50);
            message = TimeUtil.toFormattedString(message, time);
            return message;
        }
        return null;
    }

    public String getTeleportCanceled() {
        String message = teleportCanceled;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return null;
    }

    public String getDepletedQueue() {
        String message = depletedQueue;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return null;
    }

    public String getCountdown(long remainingTime) {
        String message = countdown;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            CustomTime duration = TimeUtil.formatTime(remainingTime);
            message = TimeUtil.toFormattedString(message, duration);
            return message;
        }
        return null;
    }

    public String getNoWorldPermission(World world) {
        String message = noWorldPermission;
        if (message != null) {
            message = message.replace("%world", world.getName());
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return null;
    }

    public String getEmptyQueue() {
        String message = emptyQueue;
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return null;
    }

    @Override
    public SubSectionEconomy getEconomySection() {
        return null;
    }

    @Override
    public String getInitTeleportDelay() {
        return null;
    }

    @Override
    public String getNoWorldPermission() {
        return null;
    }

    public class Economy {

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

        public String getInsufficientFunds() {
            String message = insufficientFunds;
            if (message != null) {
                message = ChatColor.translateAlternateColorCodes('&', message);
                return message;
            }
            return null;
        }

        public String getPayment() {
            String message = payment;
            if (message != null) {
                message = ChatColor.translateAlternateColorCodes('&', message);
                return message;
            }
            return null;
        }
    }

}
