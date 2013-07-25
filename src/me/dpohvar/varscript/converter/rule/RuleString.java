package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.region.Region;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleString extends ConvertRule<String>{

    public RuleString() {
        super(10);
    }

    @Override
    public <V> String convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return "";
        if (object instanceof Number) return object.toString();
        if (object instanceof Character) return object.toString();
        if (object instanceof Player) return ((Player)object).getName();
        if (object instanceof Location) {
            Location l = (Location)object;
            return l.getX()+':'+l.getY()+':'+l.getZ()+':'+l.getWorld().getName();
        }
        if (object instanceof Entity) return ((Entity)object).getType().toString();
        if (object instanceof Vector) {
            Vector v = (Vector)object;
            return v.getX()+':'+v.getY()+":"+v.getZ();
        }
        if (object instanceof Block) {
            Block b = (Block)object;
            return b.getX()+':'+b.getY()+':'+b.getZ()+':'+b.getWorld().getName();
        }
        if (object instanceof Inventory) return ((Inventory)object).getName();
        if (object instanceof ItemStack) return ((ItemStack)object).getType().name();
        if (object instanceof Collection) return StringUtils.join((Collection)object,", ");
        // if (object instanceof Map) return ((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?"TRUE":"FALSE";
        if (object instanceof Region) return object.toString();
        if (object instanceof World) return ((World) object).getName();
        if (object instanceof PotionEffect) return ((PotionEffect)object).getType().getName();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            return new String(bytes, VarScript.UTF8);
        }
        return object.toString();
	}

    @Override
    public Class<String> getClassTo() {
        return String.class;
    }
}
