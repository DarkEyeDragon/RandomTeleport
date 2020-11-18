package me.darkeyedragon.randomtp.api.queue;

public interface QueueListener<T> {
    void onAdd(T element);
    void onRemove(T element);
}
