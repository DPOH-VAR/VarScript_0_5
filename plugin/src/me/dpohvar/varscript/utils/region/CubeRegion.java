package me.dpohvar.varscript.utils.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 11:58
 */
public class CubeRegion extends Region {
    protected double x1, y1, z1;
    protected double x2, y2, z2;
    protected World world;

    public String toString() {
        return "CUBEREGION(" + x1 + ":" + y1 + ":" + z1 + "," + x2 + ":" + y2 + ":" + z2 + "," + world.getName() + ")";
    }

    @Override
    public CubeRegion clone() {
        CubeRegion c = new CubeRegion();
        c.x1 = x1;
        c.y1 = y1;
        c.z1 = z1;
        c.x2 = x2;
        c.y2 = y2;
        c.z2 = z2;
        c.world = world;
        return c;
    }

    protected CubeRegion() {
    }

    public CubeRegion(Location locA, Location locB) {
        x1 = locA.getX();
        y1 = locA.getY();
        z1 = locA.getZ();
        x2 = locB.getX();
        y2 = locB.getY();
        z2 = locB.getZ();
        if (x1 > x2) {
            Double t = x2;
            x2 = x1;
            x1 = t;
        }
        if (y1 > y2) {
            Double t = y2;
            y2 = y1;
            y1 = t;
        }
        if (z1 > z2) {
            Double t = z2;
            z2 = z1;
            z1 = t;
        }
        world = locA.getWorld();
    }

    public CubeRegion(Location loc, double distX, double distY, double distZ) {
        distX = Math.abs(distX);
        distY = Math.abs(distY);
        distZ = Math.abs(distZ);
        x1 = loc.getX() - distX / 2;
        y1 = loc.getY() - distY / 2;
        z1 = loc.getZ() - distZ / 2;
        x2 = loc.getX() + distX / 2;
        y2 = loc.getY() + distY / 2;
        z2 = loc.getZ() + distZ / 2;
        world = loc.getWorld();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Location getCenter() {
        return new Location(world, (x1 + x2) / 2, (y1 + y2) / 2, (z1 + z2) / 2);
    }

    @Override
    public boolean hasLocation(Location l) {
        if (!l.getWorld().equals(world)) return false;
        if (x2 < l.getX() || l.getX() < x1) return false;
        if (y2 < l.getY() || l.getY() < y1) return false;
        if (z2 < l.getZ() || l.getZ() < z1) return false;
        return true;
    }

    @Override
    public HashSet<Block> getBlocks() {
        HashSet<Block> blocks = new HashSet<Block>();
        int xa = (int) (Math.floor(x1)), ya = (int) (y1), za = (int) (Math.floor(z1));
        int xb = (int) (Math.floor(x2)), yb = (int) (y2), zb = (int) (Math.floor(z2));
        for (int x = xa; x <= xb; x++)
            for (int y = ya; y <= yb; y++)
                for (int z = za; z <= zb; z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
        return blocks;
    }

    @Override
    public HashSet<Block> getSolidBlocks() {
        HashSet<Block> blocks = new HashSet<Block>();
        int xa = (int) (Math.floor(x1)), ya = (int) (y1), za = (int) (Math.floor(z1));
        int xb = (int) (Math.floor(x2)), yb = (int) (y2), zb = (int) (Math.floor(z2));
        for (int x = xa; x <= xb; x++)
            for (int y = ya; y <= yb; y++)
                for (int z = za; z <= zb; z++) {
                    Block b = world.getBlockAt(x, y, z);
                    if (!b.isEmpty()) blocks.add(b);
                }
        return blocks;
    }

    @Override
    public HashSet<Block> getOutsideBlocks() {
        HashSet<Block> blocks = new HashSet<Block>();
        int xa = (int) (Math.floor(x1)), ya = (int) (y1), za = (int) (Math.floor(z1));
        int xb = (int) (Math.floor(x2)), yb = (int) (y2), zb = (int) (Math.floor(z2));
        for (int x = xa; x < xb; x++)
            for (int y = ya; y < yb; y++)
                for (int z = za; z < zb; z++) {
                    if (x == xa || x == xb - 1 || y == ya || y == yb - 1 || z == za || z == zb - 1)
                        blocks.add(world.getBlockAt(x, y, z));
                }
        return blocks;
    }
}
