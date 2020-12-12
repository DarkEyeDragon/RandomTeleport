package me.darkeyedragon.randomtp.api.addon;

import me.darkeyedragon.randomtp.api.world.location.RandomLocation;

import java.util.List;

public interface RandomLocationValidator {

    String getIdentifier();

    boolean isValid(RandomLocation location);

    List<RequiredPlugin> getRequiredPlugins();

    void setRequiredPlugins(List<RequiredPlugin> requiredPlugins);
}
