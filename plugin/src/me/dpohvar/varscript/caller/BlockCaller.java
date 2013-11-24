package me.dpohvar.varscript.caller;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class BlockCaller extends Caller {

    protected Block block;

    BlockCaller(Block block) {
        this.block = block;
    }

    @Override
    public Block getInstance() {
        return block;
    }

    @Override
    public Location getLocation() {
        return block.getLocation();
    }
}
