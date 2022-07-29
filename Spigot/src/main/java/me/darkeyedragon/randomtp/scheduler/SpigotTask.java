package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.api.scheduler.Task;
import me.darkeyedragon.randomtp.api.scheduler.TaskIdentifier;
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
    public TaskIdentifier<Integer> getTaskId() {
        return new SpigotTaskIdentifier(task.getTaskId());
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

}
