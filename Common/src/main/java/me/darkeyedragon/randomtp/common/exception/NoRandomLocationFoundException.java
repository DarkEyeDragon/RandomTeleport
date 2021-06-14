package me.darkeyedragon.randomtp.common.exception;

public class NoRandomLocationFoundException extends RuntimeException {

    private final int count;

    public NoRandomLocationFoundException(int count, String worldName) {
        super("No safe location could be found. Tried " + count + " times in " + worldName);
        this.count = count;

    }

    public int getCount() {
        return count;
    }
}
