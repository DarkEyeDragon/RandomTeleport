package me.darkeyedragon.randomtp.validator;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import org.bukkit.Location;

public class RedProtectValidator implements ChunkValidator{

    @Override
    public boolean isValid(Location location) {
        return RedProtect.get().getAPI().getRegion(location) == null;
    }
}
