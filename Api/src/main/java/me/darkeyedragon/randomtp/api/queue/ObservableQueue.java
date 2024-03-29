package me.darkeyedragon.randomtp.api.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ObservableQueue<T> extends ArrayBlockingQueue<T> {

    List<QueueListener<T>> listeners;

    public ObservableQueue(int capacity) {
        super(capacity);
        listeners = new ArrayList<>();
    }

    public ObservableQueue(int capacity, boolean fair) {
        super(capacity, fair);
    }

    public ObservableQueue(int capacity, boolean fair, Collection<? extends T> c) {
        super(capacity, fair, c);
    }

    public void subscribe(QueueListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean offer(T element) {
        if (element != null) {
            boolean changed = super.offer(element);
            listeners.forEach(listener -> listener.onAdd(element));
            return changed;
        }
        return false;
    }

    @Override
    public T poll() {
        T element = super.poll();
        if (element == null) return null;
        listeners.forEach(listener -> listener.onRemove(element));
        return element;
    }

    public void unsubscribe(QueueListener<T> listener) {
        listeners.remove(listener);
    }
}
