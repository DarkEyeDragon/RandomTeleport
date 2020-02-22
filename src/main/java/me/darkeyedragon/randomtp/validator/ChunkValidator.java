package me.darkeyedragon.randomtp.validator;

import org.bukkit.Location;


/**
 * This class is used to allow other plugins to add checks to the RTP plugin.
 * Note that every validator must have the name of the plugin + Validator as name!
 * Example: {@link FactionValidator}, {@link WorldGuardValidator}
 */
public interface ChunkValidator {
    boolean isValid(Location Location);
}
