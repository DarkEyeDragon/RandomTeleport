package me.darkeyedragon.randomtp.api.config;

import java.util.EnumSet;
import java.util.Set;

public enum Dimension {

    GLOBAL("global"), OVERWORLD("overworld"), NETHER("nether"), END("end");


    private final String type;

    Dimension(String type) {

        this.type = type;
    }

    public static EnumSet<Dimension> fromCollection(Set<String> blacklist) {
        EnumSet<Dimension> enumSet = EnumSet.noneOf(Dimension.class);
        for (String s : blacklist) {
            enumSet.add(Dimension.valueOf(s));
        }
        return enumSet;
    }

    public String getType() {
        return type;
    }

    public enum Type {
        BIOME("biome"), BLOCK("block");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
