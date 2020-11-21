package me.darkeyedragon.randomtp.common.addon;

import com.google.common.collect.ImmutableMap;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.addon.RandomAddonManager;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;
import me.darkeyedragon.randomtp.common.classloader.AddonClassLoader;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddonManager implements RandomAddonManager {

    private static final String ADDON_FOLDER_NAME = "addons";
    private static final String ABSTRACT_CLASS = "me.darkeyedragon.randomtp.common.addon.RandomAddon";

    private Map<String, RandomAddon> addons;
    private final RandomTeleportPluginImpl instance;
    private final PluginLogger logger;
    private final File folder;

    public AddonManager(RandomTeleportPluginImpl instance, PluginLogger logger) {
        this.instance = instance;
        this.logger = logger;
        this.addons = new HashMap<>();
        this.folder = new File(instance.getDataFolder(), ADDON_FOLDER_NAME);
    }

    public void instantiateAllLocal() {
        for (URL uri : getJarURIs()) {
            instantiateLocal(new AddonClassLoader(uri, instance.getClass().getClassLoader()));
        }
    }

    private void instantiateLocal(AddonClassLoader addonClassLoader) {
        try (ScanResult scanResult = new ClassGraph()
                .overrideClassLoaders(addonClassLoader)
                .acceptPackages("*.addon*")
                .scan()) {
            ClassInfoList addonClasses = scanResult.getSubclasses(ABSTRACT_CLASS);
            addons = addonClasses.loadClasses(RandomAddon.class)
                    .stream()
                    .map(this::createAddonInstance)
                    .filter(Objects::nonNull)
                    .filter(this::areRequiredPluginsPresent)
                    .filter(this::areRequiredVersionsPresent)
                    .peek(randomAddon -> logger.info(MiniMessage.get().parse("<" + NamedTextColor.GRAY + ">" + "[<" + NamedTextColor.GREEN + ">+<" + NamedTextColor.GRAY + ">] <" + NamedTextColor.LIGHT_PURPLE + ">" + randomAddon.getIdentifier() + " has been loaded")))
                    .collect(Collectors.toMap(RandomLocationValidator::getIdentifier, randomAddon -> randomAddon));
        }
    }

    private RandomAddon createAddonInstance(Class<? extends RandomAddon> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean areRequiredPluginsPresent(RandomAddon randomAddon) {
        for (RequiredPlugin requiredPlugin : randomAddon.getRequiredPlugins()) {
            if (!instance.isPluginLoaded(requiredPlugin.getName())) return false;
        }
        return true;
    }

    private boolean areRequiredVersionsPresent(RandomAddon randomAddon) {
        for (RequiredPlugin requiredPlugin : randomAddon.getRequiredPlugins()) {
            AddonPlugin addonPlugin = instance.getPlugin(requiredPlugin.getName());
            //If no version is present, assume it works for every version.
            if (requiredPlugin.getMinVersion() == null) continue;
            ComparableVersion reqMinPluginVersion = new ComparableVersion(requiredPlugin.getMinVersion());
            ComparableVersion addonPluginVersion = new ComparableVersion(addonPlugin.getVersion());
            if (requiredPlugin.getMaxVersion() != null) {
                ComparableVersion reqMaxPluginVersion = new ComparableVersion(requiredPlugin.getMaxVersion());
                if (reqMaxPluginVersion.compareTo(addonPluginVersion) > 0) return false;
            }
            if (reqMinPluginVersion.compareTo(addonPluginVersion) < 0) return false;
        }
        return true;
    }

    private URL[] getJarURIs() {
        return Arrays.stream(folder.listFiles())
                .filter(file -> file.getName().endsWith(".jar")).map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull).toArray(URL[]::new);
    }

    public RandomAddon unregister(String name) {
        throw new RuntimeException("Not implemented.");
        //return addons.remove(name);
    }

    public static String getAddonFolderName() {
        return ADDON_FOLDER_NAME;
    }

    @Unmodifiable
    public Map<String, RandomAddon> getAddons() {
        return ImmutableMap.copyOf(addons);
    }

    public RandomTeleportPluginImpl getInstance() {
        return instance;
    }

    public File getFolder() {
        return folder;
    }

    public static String getAbstractClass() {
        return ABSTRACT_CLASS;
    }
}
