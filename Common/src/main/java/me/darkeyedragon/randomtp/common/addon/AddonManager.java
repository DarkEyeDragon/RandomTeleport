package me.darkeyedragon.randomtp.common.addon;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.addon.RandomAddonManager;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;
import me.darkeyedragon.randomtp.common.addon.response.AddonResponse;
import me.darkeyedragon.randomtp.common.addon.response.AddonResponseType;
import me.darkeyedragon.randomtp.common.classloader.AddonClassLoader;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
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
        for (URL uri : getJarURLs()) {
            instantiateLocal(new AddonClassLoader(uri, instance.getClass().getClassLoader()));
        }
    }

    private void instantiateLocal(AddonClassLoader addonClassLoader) {
        try (ScanResult scanResult = new ClassGraph()
                .overrideClassLoaders(addonClassLoader)
                .acceptPackages("*.addon*")
                .scan()) {
            ClassInfoList addonClasses = scanResult.getSubclasses(ABSTRACT_CLASS);
            addons.putAll(loadAddons(addonClasses));
        }
    }

    /**
     * Create the addon folder if it doesn't exist
     */
    public boolean createAddonDir(){
        if(!folder.exists()){
            return folder.mkdir();
        }
        return false;
    }

    /**
     * @param addonClasses the {@link ClassInfoList} instance. Obtained from the {@link ScanResult}.
     * @return a collection of {@link RandomAddon} classes. This is a list since a classloader can contain multiple
     * classes which derive from {@link RandomAddon}
     */
    private Map<String, RandomAddon> loadAddons(ClassInfoList addonClasses) {
        return addonClasses.loadClasses(RandomAddon.class)
                .stream()
                .map(this::createAddonInstance)
                .filter(Objects::nonNull)
                .map(this::areRequiredPluginsPresent)
                .filter(addonResponse -> {
                    RandomAddon randomAddon = addonResponse.getAddon();
                    if (addonResponse.getResponseType() == AddonResponseType.SUCCESS) return true;
                    logger.info(MiniMessage.get().parse("<gray>" + "[<red>-<gray>] <red>" + randomAddon.getIdentifier() + " missing required plugins."));
                    for (RequiredPlugin plugin : addonResponse.getAddon().getRequiredPlugins()) {
                        if (!plugin.isLoaded()) {
                            logger.info(MiniMessage.get().parse("    └─ " + plugin.getName() + " is not loaded."));
                        }
                    }
                    return false;
                })
                .map(AddonResponse::getAddon)
                .map(this::areRequiredVersionsPresent)
                .filter(addonResponse -> {
                    RandomAddon randomAddon = addonResponse.getAddon();
                    if (addonResponse.getResponseType() == AddonResponseType.SUCCESS) return true;
                    logger.info(MiniMessage.get().parse("<gray>" + "[<red>-<gray>] <red>" + randomAddon.getIdentifier() + " version mismatch."));
                    for (RequiredPlugin plugin : addonResponse.getAddon().getRequiredPlugins()) {
                        if (!plugin.isLoaded()) {
                            logger.info(MiniMessage.get().parse("    └─ " + plugin.getName() + " with version " + plugin.getMinVersion() + " is not loaded."));
                        }
                    }
                    return false;
                })
                .map(AddonResponse::getAddon)
                .peek(randomAddon -> logger.info(MiniMessage.get().parse("<gray>" + "[<green>+<gray>] <light_purple>" + randomAddon.getIdentifier() + " has been loaded.")))
                .collect(Collectors.toMap(RandomLocationValidator::getIdentifier, randomAddon -> randomAddon));
    }

    /**
     * @param clazz the {@link Class} to instantiate. Must extend {@link RandomAddon}
     * @return the {@link } {@link RandomAddon} instance.
     */
    protected final RandomAddon createAddonInstance(Class<? extends RandomAddon> clazz) {
        try {
            RandomAddon randomAddon = clazz.getConstructor().newInstance();
            randomAddon.setAddonManager(this);
            return randomAddon;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected final AddonResponse areRequiredPluginsPresent(RandomAddon randomAddon) {
        AddonResponse addonResponse = new AddonResponse(randomAddon);
        List<RequiredPlugin> requiredPlugins = randomAddon.getRequiredPlugins();
        if (requiredPlugins.isEmpty()) {
            addonResponse.setResponseType(AddonResponseType.SUCCESS);
        }
        for (RequiredPlugin requiredPlugin : randomAddon.getRequiredPlugins()) {
            if (!instance.isPluginLoaded(requiredPlugin.getName())) {
                addonResponse.setResponseType(AddonResponseType.MISSING_DEPENDENCY);
                requiredPlugin.setLoaded(false);
            } else {
                addonResponse.setResponseType(AddonResponseType.SUCCESS);
                requiredPlugin.setLoaded(true);
            }
        }
        return addonResponse;
    }

    protected final AddonResponse areRequiredVersionsPresent(RandomAddon randomAddon) {
        AddonResponse addonResponse = new AddonResponse(randomAddon);
        List<RequiredPlugin> requiredPlugins = randomAddon.getRequiredPlugins();
        if (requiredPlugins.isEmpty()) {
            addonResponse.setResponseType(AddonResponseType.SUCCESS);
        }
        for (RequiredPlugin requiredPlugin : requiredPlugins) {
            AddonPlugin addonPlugin = instance.getPlugin(requiredPlugin.getName());
            //If no version is present, assume it works for every version.
            if (requiredPlugin.getMinVersion() == null && requiredPlugin.getMaxVersion() == null) {
                addonResponse.setResponseType(AddonResponseType.SUCCESS);
                continue;
            }
            ComparableVersion addonPluginVersion = new ComparableVersion(addonPlugin.getVersion());
            if (requiredPlugin.getMaxVersion() != null) {
                ComparableVersion reqMaxPluginVersion = new ComparableVersion(requiredPlugin.getMaxVersion());
                if (reqMaxPluginVersion.compareTo(addonPluginVersion) < 0) {
                    addonResponse.setResponseType(AddonResponseType.INVALID_MAX_VERSION);
                    requiredPlugin.setLoaded(false);
                } else {
                    addonResponse.setResponseType(AddonResponseType.SUCCESS);
                    requiredPlugin.setLoaded(true);
                }
            }
            if (requiredPlugin.getMinVersion() != null) {
                ComparableVersion reqMinPluginVersion = new ComparableVersion(requiredPlugin.getMinVersion());
                if (reqMinPluginVersion.compareTo(addonPluginVersion) > 0) {
                    addonResponse.setResponseType(AddonResponseType.INVALID_MIN_VERSION);
                    requiredPlugin.setLoaded(false);
                } else {
                    addonResponse.setResponseType(AddonResponseType.SUCCESS);
                    requiredPlugin.setLoaded(true);
                }
            }
        }
        return addonResponse;
    }
    public String[] getFileNames(){
        return Arrays.stream(folder.listFiles()).filter(file -> file.getName().endsWith(".jar")).map(File::getName).toArray(String[]::new);
    }
    protected URL[] getJarURLs() {
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

    /**
     * Unregister the addon. Unreferencing it should GC the classloader.
     *
     * @param name the identifier of the addon
     * @return the {@link RandomAddon} instance or null if not found.
     */
    @Override
    public RandomAddon unregister(String name) {
        addons.get(name).setRequiredPlugins(null);
        return addons.remove(name);
    }

    /**
     * @param name the {@link File}'s name.
     * @return the {@link RandomAddon} instance. Null if it doesnt exist
     */
    @Override
    public RandomAddon register(String name) {
        String finalName = name;
        if (!name.endsWith(".jar")) {
            finalName = name + ".jar";
        }
        File file = new File(folder, finalName);
        try {
            try (ScanResult scanResult = new ClassGraph()
                    .overrideClassLoaders(new AddonClassLoader(file.toURI().toURL(), instance.getClass().getClassLoader()))
                    .acceptPackages("*.addon*")
                    .scan()) {
                ClassInfoList addonClasses = scanResult.getSubclasses(ABSTRACT_CLASS);
                addons.putAll(loadAddons(addonClasses));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return addons.get(name);
    }

    /**
     * @return the name of the addon folder.
     * addons by default.
     */
    public static String getAddonFolderName() {
        return ADDON_FOLDER_NAME;
    }

    /**
     * @return a collection of type {@link Map} with the {@link RandomAddon} identifier as key. And the {@link RandomAddon} as value.
     */
    @Unmodifiable
    public Map<String, RandomAddon> getAddons() {
        return addons;
    }

    public RandomTeleportPluginImpl getInstance() {
        return instance;
    }

    public File getFolder() {
        return folder;
    }

    /**
     * @return the full qualified name of the object.
     */
    public static String getAbstractClass() {
        return ABSTRACT_CLASS;
    }
}
