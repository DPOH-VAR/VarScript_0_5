package org.dpohvar.varscript.utils.region;

import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class SphereOutArea extends SphereArea {

    public String toString() {
        return "SPHEREOUTAREA(" + px + ":" + pz + "," + radius + "," + world.getName() + ")";
    }

    public SphereOutArea(Location loc, double dist) {
        super(loc, dist);
    }

    public boolean hasLocation(Location l) {
        return !super.hasLocation(l);
    }

    @Override
    public SphereOutArea clone() {
        SphereOutArea c = new SphereOutArea();
        c.px = px;
        c.pz = pz;
        c.radius = radius;
        c.world = world;
        return c;
    }

    protected SphereOutArea() {
    }

}
