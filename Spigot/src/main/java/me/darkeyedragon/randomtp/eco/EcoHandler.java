package me.darkeyedragon.randomtp.eco;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class EcoHandler {

    private final Economy economy;

    public EcoHandler(Economy economy) {
        this.economy = economy;
    }
    public boolean hasEnough(Player player, double amount){
        return economy.has(player, amount);
    }
    public EconomyResponse makePayment(Player player, double amount){
        return economy.withdrawPlayer(player, amount);
    }

    public String getCurrencySingular(){
        return economy.currencyNameSingular();
    }
    public String getCurrencyPlural(){
        return economy.currencyNamePlural();
    }
}
