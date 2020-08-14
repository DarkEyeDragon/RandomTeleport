package me.darkeyedragon.randomtp.world;

public interface QueueListener<T> {
    void onAdd(T element);
    void onRemove(T element);
}