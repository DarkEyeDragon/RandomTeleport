package me.darkeyedragon.randomtp.common.stat;

import me.darkeyedragon.randomtp.api.metric.Metric;

public class BStats implements Metric {

    private static int RANDOM_TELEPORTS;

    public void addTeleportStat() {
        RANDOM_TELEPORTS++;
    }

    public int getRandomTeleportStats() {
        int result = RANDOM_TELEPORTS;
        RANDOM_TELEPORTS = 0;
        return result;
    }
}
