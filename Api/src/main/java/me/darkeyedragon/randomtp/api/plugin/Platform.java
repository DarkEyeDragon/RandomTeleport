package me.darkeyedragon.randomtp.api.plugin;

import org.jetbrains.annotations.NotNull;

public interface Platform {

    static Platform of(@NotNull String base, @NotNull String gameVersion, String implementation, String implVersion) {
        return new Platform() {
            final String baseUp = base.toUpperCase();

            @Override
            public String getBaseType() {
                return baseUp;
            }

            @Override
            public String getImplementation() {
                return implementation;
            }

            @Override
            public boolean equals(Platform platform) {
                return this.getBaseType().equalsIgnoreCase(platform.getBaseType());
            }

            @Override
            public String getGameVersion() {
                return gameVersion;
            }

            @Override
            public String getImplementationVersion() {
                return implVersion;
            }

            @Override
            public boolean matchesBaseType(String baseType) {
                return baseUp.equalsIgnoreCase(baseType);
            }

            @Override
            public String toString() {
                return String.format("Platform: %s | Game version: %s | Implementation: %s v%s", baseUp, gameVersion, implementation, implVersion);
            }
        };
    }

    /**
     * @return The base type of the platform. Example: BUKKIT, SPONGE, etc
     */
    String getBaseType();

    /**
     * @return The specific implementation of the platform. Example: Spigot, Paper, SpongeForge, SpongeVanilla, ...
     */
    String getImplementation();

    boolean equals(Platform platform);

    String getGameVersion();

    String getImplementationVersion();

    boolean matchesBaseType(String baseType);
}
