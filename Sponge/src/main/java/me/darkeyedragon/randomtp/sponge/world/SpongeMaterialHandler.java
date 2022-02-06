package me.darkeyedragon.randomtp.sponge.world;

import me.darkeyedragon.randomtp.api.world.RandomMaterial;
import me.darkeyedragon.randomtp.api.world.RandomMaterialHandler;
import me.darkeyedragon.randomtp.sponge.world.block.SpongeMaterial;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpongeMaterialHandler implements RandomMaterialHandler {

    @Override
    public RandomMaterial getMaterial(String materialName) {
        materialName = materialName.toUpperCase();
        materialName = materialName.replaceAll(" ", "_");
        return new SpongeMaterial(DummyObjectProvider.createFor(BlockType.class, materialName));
    }

    @Override
    public Set<RandomMaterial> getMaterials(Pattern pattern) {
        Set<RandomMaterial> randomMaterials = new HashSet<>();
        Collection<BlockType> blockTypes = Sponge.getRegistry().getAllOf(BlockType.class);
        for (BlockType blockType : blockTypes) {
            Matcher matcher = pattern.matcher(blockType.getName());
            if (matcher.matches()) {
                randomMaterials.add(new SpongeMaterial(blockType));
            }
        }
        return randomMaterials;
    }

    @Override
    public Set<RandomMaterial> getFromTag(String tagName) {
        //TODO IMPLEMENT
        return new HashSet<>();
    }
}
