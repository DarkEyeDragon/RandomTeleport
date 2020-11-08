package me.darkeyedragon.randomtp.validator;

/*public class WorldGuardValidator implements RandomLocationValidator {
    private final String name;
    private WorldGuard instance;
    private boolean isLoaded;

    public WorldGuardValidator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        RegionManager regions = instance.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        if (regions == null) return true;
        else {
            for (ProtectedRegion region : regions.getRegions().values()) {
                if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void load() {
        instance = WorldGuard.getInstance();
        setLoaded(instance != null);
    }

    @Override
    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

}*/
