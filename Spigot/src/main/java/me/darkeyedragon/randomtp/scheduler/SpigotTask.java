package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.api.scheduler.Task;
import org.bukkit.scheduler.BukkitTask;

public class SpigotTask implements Task {

    private final BukkitTask task;

    public SpigotTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public int getTaskId() {
        return task.getTaskId();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

}
