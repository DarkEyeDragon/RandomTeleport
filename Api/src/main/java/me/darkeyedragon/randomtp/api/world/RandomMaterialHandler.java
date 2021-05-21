package me.darkeyedragon.randomtp.api.world;

import java.util.Set;
import java.util.regex.Pattern;

public interface RandomMaterialHandler {

    RandomMaterial getMaterial(String materialName);

    Set<RandomMaterial> getMaterials(Pattern pattern);

    Set<RandomMaterial> getFromTag(String tagName);
}
