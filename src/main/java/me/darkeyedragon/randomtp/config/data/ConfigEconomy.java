package me.darkeyedragon.randomtp.config.data;

public class ConfigEconomy {


    private double price;
    private boolean useEco;
    public ConfigEconomy price(double price){
        this.price = price;
        this.useEco = price > 0;
        return this;
    }

    public double getPrice() {
        return price;
    }

    /**If the plugin is charging money to rtp.
     * NOTE: Will return false if price is not set
     * @return true if price is greater than 0
     */
    public boolean useEco(){
        return useEco;
    }
}
