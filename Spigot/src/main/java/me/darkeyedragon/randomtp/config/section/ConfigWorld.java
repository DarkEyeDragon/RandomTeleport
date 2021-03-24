package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.SpigotImpl;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.api.world.location.RandomOffset;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.stream.Collectors;


public class ConfigWorld implements SectionWorld {

    private final RandomTeleport plugin;
    private final ConfigurationSection section;
    private final Set<SectionWorldDetail> sectionWorldDetailSet;

    public ConfigWorld(SpigotImpl impl, Set<SectionWorldDetail> sectionWorldDetailsSet) {
        this.plugin = impl.getInstance();
        section = impl.getConfig().getConfigurationSection("worlds");
        this.sectionWorldDetailSet = sectionWorldDetailsSet;
    }

    @Override
    public SectionWorldDetail getSectionWorldDetail(RandomWorld randomWorld) {
        for (SectionWorldDetail sectionWorldDetail : sectionWorldDetailSet) {
            if (sectionWorldDetail.getWorld().equals(randomWorld)) {
                return sectionWorldDetail;
            }
        }
        return null;
    }

    @Override
    public ConfigWorld setSectionDetail(SectionWorldDetail sectionWorld) {
        sectionWorldDetailSet.add(sectionWorld);
        return this;
    }

    @Override
    public Set<RandomWorld> getWorlds() {
        return sectionWorldDetailSet.stream().map(SectionWorldDetail::getWorld).collect(Collectors.toSet());
    }

    @Override
    public boolean contains(RandomWorld world) {
        for (SectionWorldDetail sectionWorldDetail : sectionWorldDetailSet) {
            if (sectionWorldDetail.getWorld().equals(world)) return true;
        }
        return false;
    }

    @Override
    public boolean add(SectionWorldDetail sectionWorldDetail) {
        if (sectionWorldDetail == null) return false;
        RandomWorld randomWorld = sectionWorldDetail.getWorld();
        String worldName = randomWorld.getName();
        RandomOffset offset = sectionWorldDetail.getOffset();
        double price = sectionWorldDetail.getPrice();
        section.set(worldName + ".use_worldborder", sectionWorldDetail.useWorldBorder());
        section.set(worldName + ".needs_world_permission", sectionWorldDetail.needsWorldPermission());
        section.set(worldName + ".radius", offset.getRadius());
        section.set(worldName + ".offsetX", offset.getX());
        section.set(worldName + ".offsetZ", offset.getZ());
        section.set(worldName + ".price", price);
        plugin.getPlugin().saveConfig();
        return sectionWorldDetailSet.add(sectionWorldDetail);
    }

    @Override
    public boolean remove(RandomWorld world) {
        if (section == null || !section.contains(world.getName())) {
            return false;
        }
        section.set(world.getName(), null);
        sectionWorldDetailSet.remove(getSectionWorldDetail(world));
        plugin.getPlugin().saveConfig();
        plugin.getWorldHandler().getWorldQueue().remove(world);
        return true;
    }

    @Override
    public Set<SectionWorldDetail> getSectionWorldDetailSet() {
        return sectionWorldDetailSet;
    }
}
