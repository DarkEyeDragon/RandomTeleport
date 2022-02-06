package me.darkeyedragon.randomtp.sponge.config;

import com.google.common.io.Files;
import me.darkeyedragon.randomtp.common.config.CommonConfigHandler;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SpongeConfigHandler extends CommonConfigHandler {


    public SpongeConfigHandler(RandomTeleportPluginImpl randomTeleportPlugin, AbstractConfigurationLoader<CommentedConfigurationNode> configurationLoader) {
        super(randomTeleportPlugin, configurationLoader);
    }

    public void saveDefaultConfig() throws IOException {
        InputStream configStream = getClass().getClassLoader().getResourceAsStream("randomteleport.conf");
        File location = super.randomTeleportPlugin.getConfigPath().toFile();
        Files.createParentDirs(location);
        byte[] buffer = new byte[configStream.available()];
        configStream.read(buffer, 0, buffer.length);
        OutputStream outputStream = new FileOutputStream(location);
        outputStream.write(buffer);
        outputStream.close();
    }
}
