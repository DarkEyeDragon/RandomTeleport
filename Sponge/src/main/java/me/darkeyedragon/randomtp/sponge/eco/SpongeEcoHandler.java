package me.darkeyedragon.randomtp.sponge.eco;

import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.sponge.SpongeRandomTeleport;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SpongeEcoHandler implements EcoHandler {


    private final PluginContainer plugin;
    private final EconomyService economyService;

    public SpongeEcoHandler(PluginContainer plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
    }

    private UniqueAccount getUniqueAccount(UUID uuid){
        Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(uuid);
        return uOpt.orElse(null);
    }

    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        UniqueAccount account = getUniqueAccount(uuid);
        if (getUniqueAccount(uuid) != null) {
            BigDecimal balance = account.getBalance(economyService.getDefaultCurrency());
            return balance.doubleValue() >= amount;
        }
        return false;
    }

    @Override
    public boolean makePayment(UUID player, double amount) {
        UniqueAccount account = getUniqueAccount(player);
        if(account != null){
            account.deposit(economyService.getDefaultCurrency(), new BigDecimal(amount), Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin).build(), plugin));
        }
        return false;
    }

    @Override
    public String getCurrencySingular() {
        return economyService.getDefaultCurrency().getId();
    }

    @Override
    public String getCurrencyPlural() {
        return null;
    }
}
