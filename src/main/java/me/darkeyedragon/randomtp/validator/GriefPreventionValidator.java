package me.darkeyedragon.randomtp.validator;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;

public class GriefPreventionValidator  implements ChunkValidator {

    @Override
    public boolean isValid(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        return  claim == null;
    }
}
