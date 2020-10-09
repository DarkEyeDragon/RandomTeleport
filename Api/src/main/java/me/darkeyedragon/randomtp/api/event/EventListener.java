package me.darkeyedragon.randomtp.api.event;

public interface EventListener<T extends Event> {

    void handle(T event);

}
