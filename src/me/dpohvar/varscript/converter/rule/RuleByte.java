package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.region.Region;
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

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleByte extends ConvertRule<Byte>{

    public RuleByte() {
        super(10);
    }

    @Override
    public <V> Byte convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return 0;
        if (object instanceof Number) return ((Number)object).byteValue();
        if (object instanceof Character) return (byte)((Character)object).charValue();
        if (object instanceof Location) return (byte)((Location)object).getY();
        if (object instanceof String) return new Byte((String) object);
        if (object instanceof Entity) return (byte)((Entity)object).getEntityId();
        if (object instanceof Vector) return (byte)((Vector)object).length();
        if (object instanceof Block) return (byte)((Block)object).getTypeId();
        if (object instanceof BlockState) return (byte)((BlockState)object).getTypeId();
        if (object instanceof Inventory) return (byte)((Inventory)object).getSize();
        if (object instanceof ItemStack) return (byte)((ItemStack)object).getTypeId();
        if (object instanceof Collection) return (byte)((Collection)object).size();
        if (object instanceof NBTTagList) return (byte)((NBTTagList)object).size();
        if (object instanceof NBTTagCompound) return (byte)((NBTTagCompound)object).size();
        if (object instanceof Map) return (byte)((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?(byte)1:(byte)0;
        if (object instanceof Region) return (byte)((Region)object).getBlocks().size();
        if (object instanceof World) return (byte)Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (byte)((PotionEffect)object).getType().getId();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0;
            return bytes[0];
        }
        throw nextRule;
		// в идеале написать в конце Throw nextRule вместо return true
		
		// класс Number объединяет в себе все числовые типы: Byte,Short,Int,Long,Float,Double и другие (хз есть ли там Character)
		// делаем конвертер для каждого из них
		// а также для Vector,Entity,Player,Inventory,Collection,List,Map,Region и других.
		// в List мы конвертим инвентарь, коллекцию, Inventory, Строку и т.д.
    }

    @Override
    public Class<Byte> getClassTo() {
        return Byte.class;
    }
}
