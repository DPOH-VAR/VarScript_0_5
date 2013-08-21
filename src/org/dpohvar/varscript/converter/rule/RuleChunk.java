package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
public class RuleChunk extends ConvertRule<Chunk> {

    public RuleChunk() {
        super(10);
    }

    @Override
    public <V> Chunk convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return thread.getProgram().getCaller().getLocation().getChunk();
        if (object instanceof Number)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Number) object).doubleValue(), 0).getChunk();
        if (object instanceof Character)
            return thread.getProgram().getCaller().getLocation().add(0.0, ((Character) object), 0).getChunk();
        if (object instanceof String) return Bukkit.getPlayer(((String) object).trim()).getLocation().getChunk();
        if (object instanceof Entity) return ((Entity) object).getLocation().getChunk();
        if (object instanceof Vector)
            return thread.getProgram().getCaller().getLocation().add((Vector) object).getChunk();
        if (object instanceof Location) return ((Location) object).getChunk();
        if (object instanceof Block) return ((Block) object).getChunk();
        if (object instanceof BlockState) return ((BlockState) object).getChunk();
        if (object instanceof Inventory) {
            Object holder = ((Inventory) object).getHolder();
            return convert(holder, thread, scope);
        }
        if (object instanceof Region) return ((Region) object).getCenter().getChunk();
        if (object instanceof World) return ((World) object).getSpawnLocation().getChunk();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<Chunk> getClassTo() {
        return Chunk.class;
    }
}
