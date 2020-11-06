package me.darkeyedragon.randomtp.common.addon;

import com.google.common.collect.ImmutableMap;
import me.darkeyedragon.randomtp.api.addon.RandomAddonManager;
import me.darkeyedragon.randomtp.common.exception.addon.AddonAlreadyRegisteredException;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddonManager implements RandomAddonManager {

    private static final String ADDON_FOLDER_NAME = "addons";


    private final Map<String, RandomAddon> addons;
    private final RandomTeleportPluginImpl instance;
    private final File folder;

    public AddonManager(RandomTeleportPluginImpl instance) {
        this.instance = instance;
        addons = new HashMap<>();
        this.folder = new File(instance.getDataFolder(), ADDON_FOLDER_NAME);
    }

    public void register(Class<? extends RandomAddon> locationValidator) {
        try {
            RandomAddon addon = createAddonInstance(locationValidator);
            if(!addons.containsKey(addon.getIdentifier())){
                addons.putIfAbsent(addon.getIdentifier(), addon);
            }else{
                throw new AddonAlreadyRegisteredException(addon.getIdentifier() + " has already been registered!");
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private RandomAddon createAddonInstance(Class<? extends RandomAddon> clazz) throws ReflectiveOperationException {
        return clazz.getConstructor().newInstance();
    }


    public RandomAddon unregister(String name) {
        return addons.remove(name);
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
}
