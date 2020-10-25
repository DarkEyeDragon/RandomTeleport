package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;

public class ConfigEconomy implements SectionEconomy {

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public boolean useEco() {
        return false;
    }
}
