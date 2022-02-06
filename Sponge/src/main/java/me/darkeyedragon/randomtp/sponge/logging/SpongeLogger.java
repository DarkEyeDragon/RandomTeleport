package me.darkeyedragon.randomtp.sponge.logging;

import me.darkeyedragon.randomtp.api.logging.PluginLogger;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public class SpongeLogger implements PluginLogger {

    private final AudienceProvider audienceProvider;
    private final Logger logger;

    public SpongeLogger(AudienceProvider audienceProvider, Logger logger) {
        this.audienceProvider = audienceProvider;
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void info(Component component) {
        Component finalComponent = Component.text(PREFIX).append(component);
        audienceProvider.console().sendMessage(Identity.nil(), finalComponent);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public void severe(String s) {
        logger.error(s);
    }
}
