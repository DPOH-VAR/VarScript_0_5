package org.dpohvar.varscript.utils.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.12
 * Time: 12:07
 */
public abstract class Region implements Cloneable {
    abstract public World getWorld();

    abstract public Location getCenter();

    abstract public boolean hasLocation(Location l);

    abstract public HashSet<Block> getBlocks();

    public HashSet<Block> getBlocks(int id) {
        HashSet<Block> blocks = new HashSet<Block>();
        for (Block b : getBlocks()) {
            if (b.getTypeId() == id) blocks.add(b);
        }
        return blocks;
    }

    public HashSet<Block> getSolidBlocks() {
        HashSet<Block> blocks = getBlocks();
        blocks.removeAll(getBlocks(0));
        return blocks;
    }

    abstract public HashSet<Block> getOutsideBlocks();
}
