package me.darkeyedragon.randomtp.api.world.location;

public class ChunkLocation {

    private final int x;
    private final int z;

    /**
     * @param x the x coordinate of the chunk
     * @param z the z coordinate of the chunk
     */
    public ChunkLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * @return the x coordinate of the chunk.
     */
    public int getX() {
        return x;
    }

    /**
     * @return the z coordinate of the chunk.
     */
    public int getZ() {
        return z;
    }

}
