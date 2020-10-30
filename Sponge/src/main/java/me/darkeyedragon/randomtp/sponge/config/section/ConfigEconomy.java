package me.darkeyedragon.randomtp.sponge.config.section;

import me.darkeyedragon.randomtp.api.config.section.SectionEconomy;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConfigEconomy implements SectionEconomy {

    @Setting
    double price;

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean useEco() {
        return price > 0;
    }
}
