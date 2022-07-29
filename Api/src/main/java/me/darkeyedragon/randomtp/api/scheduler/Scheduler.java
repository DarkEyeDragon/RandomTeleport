package me.darkeyedragon.randomtp.api.scheduler;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public interface Scheduler {
    Task runTaskTimer(Runnable runnable, long delay, long interval);

    Task runTaskLater(Runnable runnable, long delay);

    void runTaskTimer(Consumer<Task> taskConsumer, long delay, long interval);

    void cancelTask(TaskIdentifier<?> taskIdentifier);

    Executor getMainThreadExecutor();
}
