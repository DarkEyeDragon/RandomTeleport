package me.darkeyedragon.randomtp.validator;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;

public class FactionValidator implements ChunkValidator {

    @Override
    public boolean isValid(Location location) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));
        return faction == FactionColl.get().getNone();
    }
}
