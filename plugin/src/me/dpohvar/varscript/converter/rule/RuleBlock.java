package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.region.Region;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleBlock extends ConvertRule<Block> {

    public RuleBlock() {
        super(10);
    }

    @Override
    public <V> Block convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Number)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Number) object).doubleValue(), 0).getBlock();
        if (object instanceof Character)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Character) object), 0).getBlock();
        if (object instanceof String) return Bukkit.getPlayer(((String) object).trim()).getLocation().getBlock();
        if (object instanceof Entity) return ((Entity) object).getLocation().getBlock();
        if (object instanceof Vector)
            return thread.getProgram().getCaller().getLocation().add((Vector) object).getBlock();
        if (object instanceof Location) return ((Location) object).getBlock();
        if (object instanceof BlockState) return ((BlockState) object).getBlock();
        if (object instanceof Inventory) {
            Object holder = ((Inventory) object).getHolder();
            return convert(holder, thread, scope);
        }
        if (object instanceof Region) return ((Region) object).getCenter().getBlock();
        if (object instanceof World) return ((World) object).getSpawnLocation().getBlock();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<Block> getClassTo() {
        return Block.class;
    }
}
