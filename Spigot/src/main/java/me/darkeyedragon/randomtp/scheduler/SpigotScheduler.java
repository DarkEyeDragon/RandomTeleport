package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.scheduler.Scheduler;

public class SpigotScheduler implements Scheduler {

    private final RandomTeleport instance;

    public SpigotScheduler(RandomTeleport instance) {
        this.instance = instance;
    }

    @Override
    public void runTaskTimer(Runnable runnable, long delay, long interval) {
        instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), runnable, delay, interval);
    }

    @Override
    public void runTaskLater(Runnable runnable, long delay) {
        instance.getPlugin().getServer().getScheduler().runTaskLater(instance.getPlugin(), runnable, delay);
    }

    @Override
    public void cancelTask(int taskId) {
        instance.getPlugin().getServer().getScheduler().cancelTask(taskId);
    }
}
