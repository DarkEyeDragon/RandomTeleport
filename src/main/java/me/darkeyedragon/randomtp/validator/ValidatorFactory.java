package me.darkeyedragon.randomtp.validator;

public class ValidatorFactory {

    public static ChunkValidator createFrom(String string) {
        return createFrom(Validator.getValidator(string));
    }

    public static ChunkValidator createFrom(Validator validator) {
        switch (validator) {
            case FACTIONS:
                return new FactionsUuidValidator();
            case WORLD_GUARD:
                return new WorldGuardValidator();
            case GRIEF_PREVENTION:
                return new GriefPreventionValidator();
        }
        return null;
    }
}
