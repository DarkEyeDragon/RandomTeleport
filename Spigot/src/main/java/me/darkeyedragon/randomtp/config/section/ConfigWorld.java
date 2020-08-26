package me.darkeyedragon.randomtp.config.section;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.config.section.SectionQueue;
import me.darkeyedragon.randomtp.api.config.section.SectionWorld;
import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.queue.LocationQueue;
import me.darkeyedragon.randomtp.api.world.RandomWorld;
import me.darkeyedragon.randomtp.world.location.WorldConfigSection;
import me.darkeyedragon.randomtp.world.location.search.LocationSearcherFactory;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.stream.Collectors;


public class ConfigWorld implements SectionWorld {

    private final RandomTeleport plugin;
    private final ConfigurationSection section;
    private final Set<SectionWorldDetail> sectionWorldDetailSet;

    public ConfigWorld(RandomTeleport plugin, Set<SectionWorldDetail> sectionWorldDetailsSet) {
        this.plugin = plugin;
        section = plugin.getConfig().getConfigurationSection("worlds");
        this.sectionWorldDetailSet = sectionWorldDetailsSet;
    }

    @Override
    public SectionWorldDetail getSectionWorldDetail(RandomWorld randomWorld) {
        for (SectionWorldDetail sectionWorldDetail : sectionWorldDetailSet) {
            if (sectionWorldDetail.getWorld() == randomWorld) {
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
            if (sectionWorldDetail.getWorld() == world) return true;
        }
        return false;
    }

    @Override
    public LocationQueue add(SectionWorldDetail sectionWorldDetail) {
        if (section == null) {
            return null;
        }
        RandomWorld randomWorld = sectionWorldDetail.getWorld();
        String worldName = randomWorld.getName();
        section.set(worldName + ".use_worldborder", sectionWorldDetail.useWorldBorder());
        section.set(worldName + ".needs_world_permission", sectionWorldDetail.needsWorldPermission());
        section.set(worldName + ".radius", sectionWorldDetail.getRadius());
        section.set(worldName + ".offsetX", sectionWorldDetail.getX());
        section.set(worldName + ".offsetZ", sectionWorldDetail.getZ());
        plugin.saveConfig();
        sectionWorldDetailSet.add(sectionWorldDetail);
        return generateLocations(randomWorld);
    }

    private LocationQueue generateLocations(RandomWorld world) {
        WorldConfigSection worldConfigSection = plugin.getLocationFactory().getWorldConfigSection(world);
        SectionQueue sectionQueue = plugin.getConfigHandler().getSectionQueue();
        LocationQueue locationQueue = new LocationQueue(sectionQueue.getSize(), LocationSearcherFactory.getLocationSearcher(world, plugin));
        plugin.subscribe(locationQueue, world);
        locationQueue.generate(worldConfigSection, sectionQueue.getSize());
        plugin.getWorldQueue().put(world, locationQueue);
        return locationQueue;
    }

    @Override
    public boolean remove(RandomWorld world) {
        if (section == null || !section.contains(world.getName())) {
            return false;
        }
        section.set(world.getName(), null);
        sectionWorldDetailSet.remove(getSectionWorldDetail(world));
        plugin.saveConfig();
        plugin.getWorldQueue().remove(world);
        return true;
    }

    @Override
    public Set<SectionWorldDetail> getSectionWorldDetailSet() {
        return sectionWorldDetailSet;
    }
}
