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
public class RuleFloat extends ConvertRule<Float>{

    public RuleFloat() {
        super(10);
    }

    @Override
    public <V> Float convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return 0f;
        if (object instanceof Number) return ((Number)object).floatValue();
        if (object instanceof Character) return (float)((Character)object).charValue();
        if (object instanceof Location) return (float)((Location)object).getY();
        if (object instanceof String) return new Float((String) object);
        if (object instanceof Entity) return (float)((Entity)object).getEntityId();
        if (object instanceof Vector) return (float)((Vector)object).length();
        if (object instanceof Block) return (float)((Block)object).getTypeId();
        if (object instanceof BlockState) return (float)((BlockState)object).getTypeId();
        if (object instanceof Inventory) return (float)((Inventory)object).getSize();
        if (object instanceof ItemStack) return (float)((ItemStack)object).getTypeId();
        if (object instanceof Collection) return (float)((Collection)object).size();
        if (object instanceof Map) return (float)((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?1f:0f;
        if (object instanceof Region) return (float)((Region)object).getBlocks().size();
        if (object instanceof World) return (float)Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (float)((PotionEffect)object).getType().getId();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof Score) return (float) ((Score)object).getScore();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0f;
            return ByteBuffer.wrap(bytes).getFloat();
        }
        throw nextRule;
	}

    @Override
    public Class<Float> getClassTo() {
        return Float.class;
    }
}
