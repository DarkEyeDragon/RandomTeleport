package me.darkeyedragon.randomtp.api.addon;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

public interface RandomLocationValidator {

    String getIdentifier();

    boolean isValid(RandomLocation location);
}