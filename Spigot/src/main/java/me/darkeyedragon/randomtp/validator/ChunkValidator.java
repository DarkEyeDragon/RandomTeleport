package me.darkeyedragon.randomtp.validator;

import org.bukkit.Location;


/**
 * This class is used to allow other plugins to add checks to the RTP plugin.
 * Note that every validator must have the name of the plugin + Validator as name!
 * Example: {@link FactionsUuidValidator}, {@link WorldGuardValidator}
 */
public interface ChunkValidator {
    boolean isValid(Location location);

    boolean isLoaded();

    void setLoaded(boolean loaded);

    Validator getValidator();
}
