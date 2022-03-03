package me.darkeyedragon.randomtp.common.config.section.subsection;

import me.darkeyedragon.randomtp.api.config.section.subsection.SubSectionEconomy;
import me.darkeyedragon.randomtp.common.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CommonSubSectionEconomy implements SubSectionEconomy {

    private String insufficientFunds;
    private String payment;

    @Override
    public Component getInsufficientFunds() {
        return ComponentUtil.toComponent(insufficientFunds);
    }

    @Override
    public Component getPayment(double price, String currency) {
        TagResolver.Single priceRes = Placeholder.unparsed("price", price + "");
        TagResolver.Single currencyRes = Placeholder.unparsed("currency", currency + "");
        return ComponentUtil.toComponent(payment, priceRes, currencyRes);
    }
}
