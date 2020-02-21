package me.darkeyedragon.randomtp.validator;

import org.bukkit.Chunk;

public class WorldGuardValidator implements ChunkValidator {
    @Override
    public boolean isValidChunk(Chunk chunk) {
        return false;
    }
}
