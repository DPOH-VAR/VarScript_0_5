package me.dpohvar.varscript.utils.region;

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

    abstract public HashSet<Block> getOutsideBlocks();
}
