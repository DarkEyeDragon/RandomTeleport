package me.darkeyedragon.randomtp.common.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

class ObservableQueue<T> extends ArrayBlockingQueue<T> {

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

    public void subscribe(QueueListener<T> listener){
        listeners.add(listener);
    }
    @Override
    public boolean offer(T element){
        boolean changed = super.offer(element);
        listeners.forEach(listener -> listener.onAdd(element));
        return changed;
    }
    @Override
    public T poll(){
        T element = super.poll();
        listeners.forEach(listener -> listener.onRemove(element));
        return element;
    }

    public void unsubscribe(QueueListener<T> listener) {
        listeners.remove(listener);
    }
}
