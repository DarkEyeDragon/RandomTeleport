package me.darkeyedragon.randomtp.validator;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.bukkit.Location;

public class GriefDefenderValidator implements ChunkValidator{

    @Override
    public boolean isValid(Location location) {
        Claim claim = GriefDefender.getCore().getClaimManager(location.getWorld().getUID()).getClaimAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return claim.isWilderness();
    }
}
