package me.darkeyedragon.randomtp.api.config.section;

public interface SectionEconomy {

    double getPrice();

    /**
     * If the plugin is charging money to rtp.
     * NOTE: Will return false if price is not set
     *
     * @return true if price is greater than 0
     */
    boolean useEco();
}
