package me.darkeyedragon.randomtp.common.eco;

import java.util.UUID;

public interface EcoHandler {

    boolean hasEnough(UUID player, double amount);
    boolean makePayment(UUID player, double amount);
    String getCurrencySingular();
    String getCurrencyPlural();
}
