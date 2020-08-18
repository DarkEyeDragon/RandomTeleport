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

import java.util.Map;
import java.util.Set;


public class ConfigWorld implements SectionWorld {

    private SectionWorld sectionWorld;
    private final RandomTeleport plugin;
    private final ConfigurationSection section;

    public ConfigWorld(RandomTeleport plugin) {
        this.plugin = plugin;
        section = plugin.getConfig().getConfigurationSection("worlds");
    }


    public ConfigWorld set(SectionWorld sectionWorld) {
        this.sectionWorld = sectionWorld;
        return this;
    }

    public SectionWorldDetail get(RandomWorld world) {
        return sectionWorld.getWorldSet().get(world);
    }

    public SectionWorld getSectionWorld() {
        return sectionWorld;
    }

    public Set<RandomWorld> getWorlds() {
        return sectionWorld.getWorldSet().keySet();
    }

    public boolean contains(RandomWorld world) {
        return sectionWorld.getWorldSet().containsKey(world);
    }

    public boolean contains(WorldConfigSection worldConfigSection) {
        return sectionWorld.getWorldSet().containsKey(worldConfigSection.getWorld());
    }

    public LocationQueue add(WorldConfigSection worldConfigSection) {
        if (section == null) {
            return null;
        }
        section.set(worldConfigSection.getWorld().getName() + ".use_worldborder", worldConfigSection.useWorldBorder());
        section.set(worldConfigSection.getWorld().getName() + ".needs_world_permission", worldConfigSection.needsWorldPermission());
        section.set(worldConfigSection.getWorld().getName() + ".radius", worldConfigSection.getRadius());
        section.set(worldConfigSection.getWorld().getName() + ".offsetX", worldConfigSection.getX());
        section.set(worldConfigSection.getWorld().getName() + ".offsetZ", worldConfigSection.getZ());
        plugin.saveConfig();
        sectionWorld.getWorldSet().put(worldConfigSection.getWorld(), worldConfigSection);
        return generateLocations(worldConfigSection.getWorld());
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

    public boolean remove(RandomWorld world) {
        if (section == null || !section.contains(world.getName())) {
            return false;
        }
        section.set(world.getName(), null);
        plugin.saveConfig();
        plugin.getWorldQueue().remove(world);
        return true;
    }

    @Override
    public Map<RandomWorld, SectionWorldDetail> getWorldSet() {
        return sectionWorld.getWorldSet();
    }
}
