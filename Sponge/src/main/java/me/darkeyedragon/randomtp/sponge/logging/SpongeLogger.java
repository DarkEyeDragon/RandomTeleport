package me.darkeyedragon.randomtp.sponge.logging;

import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import org.slf4j.Logger;

public class SpongeLogger implements PluginLogger {

    private final Logger logger;

    public SpongeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.info(s);
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
