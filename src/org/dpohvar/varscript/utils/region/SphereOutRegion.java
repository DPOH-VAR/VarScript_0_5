package org.dpohvar.varscript.utils.region;

import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class SphereOutRegion extends SphereRegion {

    public String toString() {
        return "SPHEREOUTREGION(" + px + ":" + py + ":" + pz + "," + radius + "," + world.getName() + ")";
    }

    public SphereOutRegion(Location loc, double dist) {
        super(loc, dist);
    }

    public boolean hasLocation(Location l) {
        return !super.hasLocation(l);
    }

    @Override
    public SphereOutRegion clone() {
        SphereOutRegion c = new SphereOutRegion();
        c.px = px;
        c.py = py;
        c.pz = pz;
        c.radius = radius;
        c.world = world;
        return c;
    }

    protected SphereOutRegion() {
    }
}
