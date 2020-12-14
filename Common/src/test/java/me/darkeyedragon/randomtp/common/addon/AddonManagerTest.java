package me.darkeyedragon.randomtp.common.addon;

import me.darkeyedragon.randomtp.api.addon.AddonPlugin;
import me.darkeyedragon.randomtp.api.addon.RequiredPlugin;
import me.darkeyedragon.randomtp.api.config.RandomConfigHandler;
import me.darkeyedragon.randomtp.api.queue.WorldQueue;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.common.addon.response.AddonResponse;
import me.darkeyedragon.randomtp.common.addon.response.AddonResponseType;
import me.darkeyedragon.randomtp.common.eco.EcoHandler;
import me.darkeyedragon.randomtp.common.logging.PluginLogger;
import me.darkeyedragon.randomtp.common.plugin.RandomTeleportPluginImpl;
import me.darkeyedragon.randomtp.common.world.location.LocationFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertSame;

class AddonManagerTest {

    private AddonManager addonManager;
    private RandomTeleportPluginImpl impl;
    private RequiredPlugin requiredPluginNoMinMaxVersion;
    private RequiredPlugin requiredPluginNoMaxVersion;
    private AddonPlugin addonPlugin;

    @BeforeEach
    void setUp() {
        impl = new RandomTeleportPluginImpl() {
            @Override
            public AddonManager getAddonManager() {
                return addonManager;
            }

            @Override
            public AddonPlugin getPlugin(String name) {
                return addonPlugin;
            }

            @Override
            public PluginLogger getLogger() {
                return null;
            }

            @Override
            public EcoHandler getEcoHandler() {
                return null;
            }

            @Override
            public boolean setupEconomy() {
                return false;
            }

            @Override
            public RandomConfigHandler getConfigHandler() {
                return null;
            }

            @Override
            public WorldQueue getWorldQueue() {
                return null;
            }

            @Override
            public LocationFactory getLocationFactory() {
                return null;
            }

            @Override
            public RandomTeleportPluginImpl getInstance() {
                return this;
            }

            @Override
            public File getDataFolder() {
                return null;
            }

            @Override
            public boolean isPluginLoaded(String name) {
                return false;
            }
        };
        requiredPluginNoMinMaxVersion = new RequiredPlugin() {
            private boolean loaded;
            @Override
            public String getMinVersion() {
                return null;
            }

            @Override
            public String getMaxVersion() {
                return null;
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void setLoaded(boolean b) {
                loaded = b;
            }

            @Override
            public String getName() {
                return "requiredPluginNoMinMaxVersion";
            }
        };
        requiredPluginNoMaxVersion = new RequiredPlugin() {
            private boolean loaded;

            @Override
            public String getMinVersion() {
                return "1.5.5";
            }

            @Override
            public String getMaxVersion() {
                return null;
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void setLoaded(boolean b) {
                loaded = b;
            }

            @Override
            public String getName() {
                return "requiredPluginNoMaxVersion";
            }
        };
        addonPlugin = new AddonPlugin() {
            @Override
            public String getVersion() {
                return "1.5.5";
            }

            @Override
            public String getName() {
                return "TestAddonPlugin";
            }
        };
        addonManager = new AddonManager(impl, null);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Do addons who have no required plugins pass as valid.
     */
    @Test
    void areRequiredPluginsPresentNone() {
        RandomAddon addon1 = new RandomAddon() {
            @Override
            public String getIdentifier() {
                return "TestAddon1";
            }

            @Override
            public boolean isValid(RandomLocation location) {
                return false;
            }
        };
        assertSame(AddonResponseType.SUCCESS, addonManager.areRequiredPluginsPresent(addon1).getResponseType());
    }

    @Test
    void areRequiredVersionsPresentNoMinMax() {
        RandomAddon randomAddon = new RandomAddon() {
            @Override
            public String getIdentifier() {
                return "areRequiredVersionsPresentNoMinMax";
            }

            @Override
            public boolean isValid(RandomLocation location) {
                return false;
            }
        };
        randomAddon.addRequiredPlugin(requiredPluginNoMinMaxVersion);
        assertSame(AddonResponseType.SUCCESS, addonManager.areRequiredVersionsPresent(randomAddon).getResponseType());
    }

    @Test
    void areRequiredVersionsPresentSameMinVersion() {
        RandomAddon randomAddon = new RandomAddon() {
            @Override
            public String getIdentifier() {
                return "areRequiredVersionsPresentSameMinVersion";
            }

            @Override
            public boolean isValid(RandomLocation location) {
                return false;
            }
        };
        randomAddon.addRequiredPlugin(requiredPluginNoMaxVersion);

        assertSame(AddonResponseType.SUCCESS, addonManager.areRequiredVersionsPresent(randomAddon).getResponseType(), "test");
    }

    /**
     * This test validates if the version validator fails if the plugin version
     * is lower than the minimum plugin version.
     */
    @Test
    void areRequiredVersionsPresentLowerMinVersion() {
        RandomAddon randomAddon = new RandomAddon() {
            @Override
            public String getIdentifier() {
                return "areRequiredVersionsPresentLowerMinVersion";
            }

            @Override
            public boolean isValid(RandomLocation location) {
                return false;
            }
        };
        RequiredPlugin requiredPlugin = new BaseRequiredPlugin("LowerMinVersion", "1.9.5");
        randomAddon.addRequiredPlugin(requiredPlugin);
        AddonResponse response = addonManager.areRequiredVersionsPresent(randomAddon);
        assertSame(AddonResponseType.INVALID_MIN_VERSION, response.getResponseType(), "Min version " + requiredPlugin.getMinVersion() + " | Actual version " + addonPlugin.getVersion());    }

    /**
     * This test validates if the version validator fails if the plugin version
     * is higher than the max plugin version.
     */
    @Test
    void areRequiredVersionsPresentHigherMaxVersion() {
        RandomAddon randomAddon = new RandomAddon() {
            @Override
            public String getIdentifier() {
                return "areRequiredVersionsPresentLowerMinVersion";
            }

            @Override
            public boolean isValid(RandomLocation location) {
                return false;
            }
        };
        RequiredPlugin requiredPlugin = new BaseRequiredPlugin("LowerMinVersion", null, "1.1.0");
        randomAddon.addRequiredPlugin(requiredPlugin);
        AddonResponse response = addonManager.areRequiredVersionsPresent(randomAddon);
        assertSame(AddonResponseType.INVALID_MAX_VERSION, response.getResponseType(), "Max version " + requiredPlugin.getMaxVersion() + " | Actual version " + addonPlugin.getVersion());
    }
}