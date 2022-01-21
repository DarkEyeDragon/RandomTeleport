package me.darkeyedragon.randomtp.api.config.section.subsection;

import me.darkeyedragon.randomtp.api.world.RandomWorld;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface SubSectionSign extends SubSection {

    List<Component> getComponents(RandomWorld world);

    SubSectionSign setComponents(List<String> lines);

}
