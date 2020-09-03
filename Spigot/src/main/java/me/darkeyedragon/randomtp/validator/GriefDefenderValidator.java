package me.darkeyedragon.randomtp.validator;

/*import com.griefdefender.api.Core;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.bukkit.Location;

public class GriefDefenderValidator implements ChunkValidator {

    private final Core instance;
    private boolean isLoaded;

    public GriefDefenderValidator() {
        instance = GriefDefender.getCore();
        this.isLoaded = instance != null;
    }

    @Override
    public boolean isValid(Location location) {
        Claim claim = instance.getClaimManager(location.getWorld().getUID()).getClaimAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return claim.isWilderness();
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    @Override
    public Validator getValidator() {
        return Validator.GRIEF_DEFENDER;
    }
}
*/