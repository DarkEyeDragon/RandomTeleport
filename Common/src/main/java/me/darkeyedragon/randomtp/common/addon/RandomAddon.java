package me.darkeyedragon.randomtp.common.addon;

import me.darkeyedragon.randomtp.api.addon.RandomLocationValidator;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public class RandomAddon implements RandomLocationValidator {

    private final String identifier;

    public RandomAddon(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isRegistered(RandomLocation location) {
        return false;
    }

    @Override
    public boolean unregister() {
        return false;
    }

    @Override
    public void register() {

    }
}
