package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.region.Region;
import me.dpohvar.varscript.vs.Scope;
import me.dpohvar.varscript.vs.Thread;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleInteger extends ConvertRule<Integer>{

    public RuleInteger() {
        super(10);
    }

    @Override
    public <V> Integer convert(V object, Thread thread,Scope scope) throws NextRule {
        if (object==null) return 0;
        if (object instanceof Number) return ((Number)object).intValue();
        if (object instanceof Character) return (int)((Character)object).charValue();
        if (object instanceof Location) return (int)((Location)object).getY();
        if (object instanceof String) return new Integer((String) object);
        if (object instanceof Entity) return ((Entity)object).getEntityId();
        if (object instanceof Vector) return (int)((Vector)object).length();
        if (object instanceof Block) return ((Block)object).getTypeId();
        if (object instanceof BlockState) return ((BlockState)object).getTypeId();
        if (object instanceof Inventory) return ((Inventory)object).getSize();
        if (object instanceof ItemStack) return ((ItemStack)object).getTypeId();
        if (object instanceof Collection) return ((Collection)object).size();
        if (object instanceof Map) return ((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?1:0;
        if (object instanceof Region) return ((Region)object).getBlocks().size();
        if (object instanceof World) return Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return ((PotionEffect)object).getType().getId();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof Score) return (int) ((Score)object).getScore();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0;
            return ByteBuffer.wrap(bytes).getInt();
        }
        throw nextRule;
    }

    @Override
    public Class<Integer> getClassTo() {
        return Integer.class;
    }
}
