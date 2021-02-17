package me.darkeyedragon.randomtp.common.exception;

import me.darkeyedragon.randomtp.api.world.RandomWorld;

public class NoRandomLocationFoundException extends RuntimeException {

    private final int count;

    public NoRandomLocationFoundException(int count, RandomWorld world) {
        super("No safe location could be found. Tried " + count + " times in " + world.getName());
        this.count = count;

    }

    public int getCount() {
        return count;
    }
}
