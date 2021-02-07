package me.darkeyedragon.randomtp.api.scheduler;

public interface Scheduler {
    void runTaskTimer(Runnable runnable, long delay, long interval);

    void runTaskLater(Runnable runnable, long delay);

    void cancelTask(int taskId);
}
