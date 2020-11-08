package me.darkeyedragon.randomtp.validator;

/**
 * Faction validator for https://www.spigotmc.org/resources/factionsuuid.1035
 */
/*public class FactionsUuidValidator implements RandomLocationValidator {

    private final String name;
    private Board instance;
    private boolean isLoaded;

    public FactionsUuidValidator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(RandomLocation location) {
        Location loc = WorldUtil.toLocation(location);
        FLocation fLocation = new FLocation(loc);
        Faction faction = instance.getFactionAt(fLocation);
        return faction.isWilderness() || faction.isWarZone();
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void load() {
        instance = Board.getInstance();
        setLoaded(instance != null);
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

}*/
