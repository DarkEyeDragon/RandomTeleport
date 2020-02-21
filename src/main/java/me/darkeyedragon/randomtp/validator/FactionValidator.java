package me.darkeyedragon.randomtp.validator;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;

public class FactionValidator implements ChunkValidator {

    @Override
    public boolean isValidChunk(Chunk chunk) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(chunk));
        return faction == FactionColl.get().getNone();
    }
}
