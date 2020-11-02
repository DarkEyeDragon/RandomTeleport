package me.darkeyedragon.randomtp.stat;

public class BStats {
    private static int RANDOM_TELEPORTS;

    public static void addTeleportStat(){
        RANDOM_TELEPORTS++;
    }

    public static int getRandomTeleportStats() {
        int result = RANDOM_TELEPORTS;
        RANDOM_TELEPORTS = 0;
        return result;
    }
}
