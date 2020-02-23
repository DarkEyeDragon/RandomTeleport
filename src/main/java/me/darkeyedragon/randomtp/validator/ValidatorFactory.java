package me.darkeyedragon.randomtp.validator;

public class ValidatorFactory {

    public static ChunkValidator createFrom(String string){
        return createFrom(Validator.valueOf(string));
    }
    public static ChunkValidator createFrom(Validator validator){
        switch (validator){
            case Factions:
                return new FactionsUuidValidator();
            case WorldGuard:
                return new WorldGuardValidator();
            default:
                return null;
        }
    }
}
