package me.darkeyedragon.randomtp.common.queue;

public interface QueueListener<T> {
    void onAdd(T element);
    void onRemove(T element);
}
