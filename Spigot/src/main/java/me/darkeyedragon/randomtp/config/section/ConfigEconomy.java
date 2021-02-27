package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;

public class ConfigEconomy implements SectionEconomy {

    private double price;

    public ConfigEconomy price(double price) {
        this.price = price;
        return this;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * If the plugin is charging money to rtp.
     * NOTE: Will return false if price is not set
     *
     * @return true if price is greater than 0
     */
    public boolean useEco() {
        return price > 0;
    }

}
