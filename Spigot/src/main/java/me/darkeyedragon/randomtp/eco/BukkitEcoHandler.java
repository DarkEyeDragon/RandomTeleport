package me.darkeyedragon.randomtp.eco;

import me.darkeyedragon.randomtp.api.eco.EcoHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BukkitEcoHandler implements EcoHandler {

    private final Economy economy;

    public BukkitEcoHandler(Economy economy) {
        this.economy = economy;
    }

    @Override
    public boolean hasEnough(UUID playerUUID, double amount) {
        return economy.has(Bukkit.getPlayer(playerUUID), amount);
    }

    @Override
    public boolean makePayment(UUID playerUUID, double amount) {
        return economy.withdrawPlayer(Bukkit.getPlayer(playerUUID), amount).transactionSuccess();
    }

    @Override
    public String getCurrencySingular() {
        return economy.currencyNameSingular();
    }

    @Override
    public String getCurrencyPlural() {
        return economy.currencyNamePlural();
    }
}
