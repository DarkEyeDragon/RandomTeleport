package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.scheduler.Scheduler;
import me.darkeyedragon.randomtp.api.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class SpigotScheduler implements Scheduler {

    private final RandomTeleport instance;

    public SpigotScheduler(RandomTeleport instance) {
        this.instance = instance;
    }

    @Override
    public Task runTaskTimer(Runnable runnable, long delay, long interval) {
        BukkitTask task = instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), runnable, delay, interval);
        return toTask(task);
    }

    @Override
    public Task runTaskLater(Runnable runnable, long delay) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(instance.getPlugin(), runnable, delay);
        return new Task() {
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
        };
    }

    //TODO Trigary fix pl0x
    @Override
    public void runTaskTimer(Consumer<Task> taskConsumer, long delay, long interval) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Bukkit.getScheduler().runTaskTimer(instance.getPlugin(), runnable, delay, interval);
    }

    private Task toTask(BukkitTask bukkitTask) {
        return new Task() {
            @Override
            public void cancel() {
                bukkitTask.cancel();
            }

            @Override
            public int getTaskId() {
                return bukkitTask.getTaskId();
            }

            @Override
            public boolean isCancelled() {
                return bukkitTask.isCancelled();
            }
        };
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
