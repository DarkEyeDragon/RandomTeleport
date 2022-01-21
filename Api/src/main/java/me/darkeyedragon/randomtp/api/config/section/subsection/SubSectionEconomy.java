package me.darkeyedragon.randomtp.api.config.section.subsection;

import net.kyori.adventure.text.Component;

public interface SubSectionEconomy extends SubSection {
    Component getInsufficientFunds();

    Component getPayment(double price, String currency);
}
