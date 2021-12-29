package me.darkeyedragon.randomtp.scheduler;

import me.darkeyedragon.randomtp.RandomTeleport;
import me.darkeyedragon.randomtp.api.scheduler.Scheduler;
import me.darkeyedragon.randomtp.api.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class SpigotScheduler implements Scheduler {

    private final RandomTeleport instance;

    public SpigotScheduler(RandomTeleport instance) {
        this.instance = instance;
    }

    @Override
    public Task runTaskTimer(Runnable runnable, long delay, long interval) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(instance.getPlugin(), runnable, delay, interval);
        return toTask(task);
    }

    @Override
    public Task runTaskLater(Runnable runnable, long delay) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(instance.getPlugin(), runnable, delay);
        return toTask(task);
    }

    @Override
    public void runTaskTimer(Consumer<Task> taskConsumer, long delay, long interval) {
        new BukkitRunnable() {
            private final Task task = toTask(this);

            @Override
            public void run() {
                taskConsumer.accept(task);
            }
        }.runTaskTimer(instance.getPlugin(), delay, interval);
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

    private Task toTask(BukkitRunnable bukkitRunnable) { //TODO ded be happy
        return new Task() {
            @Override
            public void cancel() {
                bukkitRunnable.cancel();
            }

            @Override
            public int getTaskId() {
                return bukkitRunnable.getTaskId();
            }

            @Override
            public boolean isCancelled() {
                return bukkitRunnable.isCancelled();
            }
        };
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public Executor getMainThreadExecutor() {
        return this::runTask;
    }

    public void runTask(Runnable command) {
        instance.getScheduler().runTaskLater(command, 0L);
    }
}
