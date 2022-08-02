package me.darkeyedragon.randomtp.sponge.scheduler;

import me.darkeyedragon.randomtp.api.scheduler.Scheduler;
import me.darkeyedragon.randomtp.api.scheduler.Task;
import me.darkeyedragon.randomtp.api.scheduler.TaskIdentifier;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SpongeScheduler implements Scheduler {

    private final PluginContainer plugin;
    private final org.spongepowered.api.scheduler.Scheduler scheduler;

    public SpongeScheduler(PluginContainer plugin, org.spongepowered.api.scheduler.Scheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    @Override
    public Task runTaskTimer(Runnable runnable, long delay, long interval) {
        org.spongepowered.api.scheduler.Task task = org.spongepowered.api.scheduler.Task
                .builder()
                .delay(delay / 50, TimeUnit.MILLISECONDS)
                .interval(interval / 50, TimeUnit.MILLISECONDS)
                .execute(runnable)
                .submit(plugin);
        return toTask(task);
    }

    @Override
    public Task runTaskLater(Runnable runnable, long delay) {
        org.spongepowered.api.scheduler.Task task = org.spongepowered.api.scheduler.Task
                .builder()
                .delay(delay / 50, TimeUnit.MILLISECONDS)
                .execute(runnable)
                .submit(plugin);
        return toTask(task);
    }

    @Override
    public void runTaskTimer(Consumer<Task> taskConsumer, long delay, long interval) {
        org.spongepowered.api.scheduler.Task
                .builder()
                .delay(delay * 50, TimeUnit.MILLISECONDS)
                .interval(interval * 50, TimeUnit.MILLISECONDS)
                .execute(task -> taskConsumer.accept(toTask(task)))
                .submit(plugin);
    }

    @Override
    public void cancelTask(TaskIdentifier<?> taskIdentifier) {
        scheduler.getTaskById((UUID) taskIdentifier.getIdentifier()).ifPresent(org.spongepowered.api.scheduler.Task::cancel);
    }

    @Override
    public Executor getMainThreadExecutor() {
        return this::runTask;
    }

    private Task toTask(org.spongepowered.api.scheduler.Task task) {
        return new Task() {
            @Override
            public void cancel() {
                task.cancel();
            }

            @Override
            public TaskIdentifier<UUID> getTaskId() {
                return new SpongeTaskIdentifier(task.getUniqueId());
            }

            @Override
            public boolean isCancelled() {
                return !scheduler.getScheduledTasks().contains(task);
            }
        };
    }

    public void runTask(Runnable command) {
        runTaskLater(command, 0L);
    }

}
