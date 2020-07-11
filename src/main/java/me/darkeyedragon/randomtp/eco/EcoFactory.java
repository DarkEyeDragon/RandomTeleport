package me.darkeyedragon.randomtp.eco;

import me.darkeyedragon.randomtp.exception.EcoNotSupportedException;
import net.milkbowl.vault.economy.Economy;

public class EcoFactory {

    private static EcoHandler ecoHandler;

    public static void createDefault(Economy economy) {
        ecoHandler = new EcoHandler(economy);
    }

    public static EcoHandler getInstance() throws EcoNotSupportedException {
        if (ecoHandler == null)
            throw new EcoNotSupportedException("There is no economy registered. Make sure Vault is installed if you want to use economy related features.");
        return ecoHandler;
    }

    public static boolean isUseEco() {
        return ecoHandler != null;
    }
}
