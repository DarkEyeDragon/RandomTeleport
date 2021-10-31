package me.darkeyedragon.randomtp.common.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSectionEconomy implements SectionEconomy {

    private double defaultPrice;

    @Override
    public double getPrice() {
        return defaultPrice;
    }

    @Override
    public void setPrice(double price) {
        this.defaultPrice = price;
    }
}
