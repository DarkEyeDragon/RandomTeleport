package me.darkeyedragon.randomtp.api.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class EventBus {

    private Map<Class, List<EventListener>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    public void emit(Event event) {
        Class eventClass = event.getClass();
        List<EventListener> eventListeners = listeners.get(eventClass);
        for (EventListener eventListener : eventListeners) {
            eventListener.handle(event);
        }
    }

    public <T extends Event> void listenFor(Class<T> eventClass, EventListener<T> listener) {
        if (!listeners.containsKey(eventClass)) {
            listeners.put(eventClass, new LinkedList<>());
        }
        listeners.get(eventClass).add(listener);
    }
}
