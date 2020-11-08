package me.darkeyedragon.randomtp.common.addon;

import com.google.common.collect.ImmutableMap;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.darkeyedragon.randomtp.api.addon.RandomAddonManager;
import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.common.classloader.AddonClassLoader;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AddonManager implements RandomAddonManager {

    private static final String ADDON_FOLDER_NAME = "addons";
    private static final String INTERFACE_NAME = "me.darkeyedragon.randomtp.api.addon.RandomLocationValidator";

    private Map<String, RandomAddon> addons;
    private final RandomTeleportPluginImpl instance;
    private final File folder;
    private final AddonClassLoader addonClassLoader;

    public AddonManager(RandomTeleportPluginImpl instance) {
        this.instance = instance;
        this.addons = new HashMap<>();
        this.folder = new File(instance.getDataFolder(), ADDON_FOLDER_NAME);
        addonClassLoader = new AddonClassLoader();
    }

    /*public void register(Class<? extends RandomAddon> locationValidator) {
        try {
            RandomAddon addon = createAddonInstance(locationValidator);
            if(!addons.containsKey(addon.getIdentifier())){
                addons.putIfAbsent(addon.getIdentifier(), addon);
            }else{
                throw new AddonAlreadyRegisteredException(addon.getIdentifier() + " has already been registered!");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }*/

    public void instantiateAllLocal(){
        try(ScanResult scanResult = new ClassGraph().enableAllInfo().overrideClassLoaders(new AddonClassLoader()).scan()) {
            ClassInfoList addonClasses = scanResult.getClassesImplementing(INTERFACE_NAME);
            addons = addonClasses.loadClasses(RandomAddon.class)
                    .stream()
                    .map(this::createAddonInstance)
                    .collect(Collectors.toMap(RandomLocationValidator::getIdentifier, randomAddon -> randomAddon));
        }
    }

    private RandomAddon createAddonInstance(Class<? extends RandomAddon> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e.getCause());
        }
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

    public static String getInterfaceName() {
        return INTERFACE_NAME;
    }

    public AddonClassLoader getAddonClassLoader() {
        return addonClassLoader;
    }
}
