package me.darkeyedragon.randomtp.common.util;

public enum Direction {
    NORTH(0, -1, 0),
    NORTH_EAST(1, -1, 1),
    EAST(2, 0, 1),
    SOUTH_EAST(3, 1, 1),
    SOUTH(4, 1, 0),
    SOUTH_WEST(5, 1, 1),
    WEST(6, 0, -1),
    NORTH_WEST(7, -1, -1);

    private final int id;
    private final int x;
    private final int z;

    Direction(int id, int x, int z) {
        this.id = id;
        this.x = x;
        this.z = z;
    }

    public static Direction getDirection(int id) {
        for (Direction direction : values()) {
            if (direction.id == id) return direction;
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getId() {
        return id;
    }
}
