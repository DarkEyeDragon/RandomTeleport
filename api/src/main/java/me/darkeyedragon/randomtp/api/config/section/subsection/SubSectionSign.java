package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface SubSectionSign {

    List<Component> getComponents(RandomWorld world);

    SubSectionSign setComponents(List<String> lines);

}
