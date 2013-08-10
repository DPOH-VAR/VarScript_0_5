package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.bukkit.inventory.Inventory;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleVector extends ConvertRule<Vector>{

    public RuleVector() {
        super(10);
    }

    @Override
    public <V> Vector convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return new Vector();
        if (object instanceof Number) return new Vector(0,((Number)object).doubleValue(),0);
        if (object instanceof String) return convert(Bukkit.getPlayer(((String)object).trim()).getLocation(),thread,scope);
        if (object instanceof Entity) return convert(((Entity)object).getLocation(),thread,scope);
        if (object instanceof Block) return convert(((Block)object).getLocation(),thread,scope);
        if (object instanceof BlockState) return convert(((BlockState)object).getLocation(),thread,scope);
        if (object instanceof Inventory) return convert(((Inventory)object).getHolder(),thread,scope);
        if (object instanceof OfflinePlayer) return convert(((OfflinePlayer)object).getPlayer().getLocation(),thread,scope);
        if (object instanceof Location) {
            Location a = thread.getProgram().getCaller().getLocation();
            Location b = (Location) object;
            if(!b.getWorld().equals(a.getWorld())) return null;
            return b.toVector().subtract(a.toVector());
        }
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);

        throw nextRule;
    }

    @Override
    public Class<Vector> getClassTo() {
        return Vector.class;
    }
}
