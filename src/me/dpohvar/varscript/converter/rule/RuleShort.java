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
public class RuleShort extends ConvertRule<Short>{

    public RuleShort() {
        super(10);
    }

    @Override
    public <V> Short convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return 0;
        if (object instanceof Number) return ((Number)object).shortValue();
        if (object instanceof Character) return (short)((Character)object).charValue();
        if (object instanceof Location) return (short)((Location)object).getY();
        if (object instanceof String) return new Short((String) object);
        if (object instanceof Entity) return (short)((Entity)object).getEntityId();
        if (object instanceof Vector) return (short)((Vector)object).length();
        if (object instanceof Block) return (short)((Block)object).getTypeId();
        if (object instanceof BlockState) return (short)((BlockState)object).getTypeId();
        if (object instanceof Inventory) return (short)((Inventory)object).getSize();
        if (object instanceof ItemStack) return (short)((ItemStack)object).getTypeId();
        if (object instanceof Collection) return (short)((Collection)object).size();
        if (object instanceof Map) return (short)((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?(short)1:(short)0;
        if (object instanceof Region) return (short)((Region)object).getBlocks().size();
        if (object instanceof World) return (short)Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (short)((PotionEffect)object).getType().getId();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0;
            return ByteBuffer.wrap(bytes).getShort();
        }
        throw nextRule;
    }

    @Override
    public Class<Short> getClassTo() {
        return Short.class;
    }
}
