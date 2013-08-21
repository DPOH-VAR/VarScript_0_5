package org.dpohvar.varscript.utils.region;

import org.bukkit.Location;

/**
 * Created by IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 13:06
 */
public class CubeOutRegion extends CubeRegion {

    public String toString() {
        return "CUBEOUTREGION(" + x1 + ":" + y1 + ":" + z1 + "," + x2 + ":" + y2 + ":" + z2 + "," + world.getName() + ")";
    }

    public CubeOutRegion(Location locA, Location locB) {
        super(locA, locB);
    }

    public CubeOutRegion(Location loc, double distX, double distY, double distZ) {
        super(loc, distX, distY, distZ);
    }

    public boolean hasLocation(Location l) {
        return !super.hasLocation(l);
    }
}
