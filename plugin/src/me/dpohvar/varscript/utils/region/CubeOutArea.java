package me.dpohvar.varscript.utils.region;

import org.bukkit.Location;

/**
 * Created by IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class CubeOutArea extends CubeArea {

    public CubeOutArea(Location locA, Location locB) {
        super(locA, locB);
    }

    public CubeOutArea(Location loc, double distX, double distZ) {
        super(loc, distX, distZ);
    }

    public String toString() {
        return "CUBEOUTAREA(" + x1 + ":" + z1 + "," + x2 + ":" + z2 + "," + world.getName() + ")";
    }

    public boolean hasLocation(Location l) {
        return !super.hasLocation(l);
    }

    @Override
    public CubeOutArea clone() {
        CubeOutArea c = new CubeOutArea();
        c.x1 = x1;
        c.z1 = z1;
        c.x2 = x2;
        c.z2 = z2;
        c.world = world;
        return c;
    }

    protected CubeOutArea() {
    }
}
