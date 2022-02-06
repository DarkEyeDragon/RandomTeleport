package me.darkeyedragon.randomtp.api.scheduler;


public interface Task {
    void cancel();

    TaskIdentifier<?> getTaskId();

    boolean isCancelled();

}
