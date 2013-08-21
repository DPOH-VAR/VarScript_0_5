package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.utils.region.Region;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleBlockState extends ConvertRule<BlockState> {

    public RuleBlockState() {
        super(10);
    }

    @Override
    public <V> BlockState convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Block) return ((Block) object).getState();
        if (object instanceof Number)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Number) object).doubleValue(), 0).getBlock().getState();
        if (object instanceof Character)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Character) object), 0).getBlock().getState();
        if (object instanceof String)
            return Bukkit.getPlayer(((String) object).trim()).getLocation().getBlock().getState();
        if (object instanceof Entity) return ((Entity) object).getLocation().getBlock().getState();
        if (object instanceof Vector)
            return thread.getProgram().getCaller().getLocation().add((Vector) object).getBlock().getState();
        if (object instanceof Location) return ((Location) object).getBlock().getState();
        if (object instanceof Inventory) {
            Object holder = ((Inventory) object).getHolder();
            return convert(holder, thread, scope);
        }
        if (object instanceof Region) return ((Region) object).getCenter().getBlock().getState();
        if (object instanceof World) return ((World) object).getSpawnLocation().getBlock().getState();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<BlockState> getClassTo() {
        return BlockState.class;
    }
}
