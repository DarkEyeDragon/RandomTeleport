package me.darkeyedragon.randomtp.api.scheduler;


public interface Task {
    void cancel();

    int getTaskId();

    boolean isCancelled();
}
