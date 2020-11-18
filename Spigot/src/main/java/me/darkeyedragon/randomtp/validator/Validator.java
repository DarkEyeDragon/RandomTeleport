package me.darkeyedragon.randomtp.validator;

import me.darkeyedragon.randomtp.api.addon.PluginLocationValidator;

public enum Validator {
    FACTIONS(new FactionsUuidValidator("Factions")),
    WORLD_GUARD(new WorldGuardValidator("WorldGuard")),
    GRIEF_PREVENTION(new GriefPreventionValidator("GriefPrevention")),
    TOWNY(new TownyValidator("Towny")),
    RED_PROTECT(new RedProtectValidator("RedProtect"));

    final PluginLocationValidator validator;

    Validator(PluginLocationValidator validator) {
        this.validator = validator;
    }

    public static PluginLocationValidator getValidator(String name) {
        for (Validator validator : Validator.values()) {
            if (validator.validator.getName().equalsIgnoreCase(name)) {
                return validator.validator;
            }
        }
        return null;
    }

    public String getName() {
        return validator.getName();
    }
}
