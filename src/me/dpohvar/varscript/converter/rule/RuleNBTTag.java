package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.*;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.reflect.NBTTagWrapper;
import me.dpohvar.varscript.utils.region.Region;
import me.dpohvar.varscript.vs.Scope;
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
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleNBTTag extends ConvertRule<NBTBase>{

    public RuleNBTTag() {
        super(10);
    }

    @Override
    public <V> NBTBase convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof NBTTagWrapper) return ((NBTTagWrapper)object).getTag();
        if (object instanceof Number) return NBTBase.getByValue(object);
        if (object instanceof int[]) return new NBTTagIntArray((int[])object);
        if (object instanceof byte[]) return new NBTTagByteArray((byte[])object);
        if (object instanceof String) return new NBTTagString((String)object);
        if (object instanceof Map) {
            Map map = (Map)object;
            NBTTagCompound compound = new NBTTagCompound();
            for(Object key:map.keySet()){
                compound.set(key.toString(),convert(map.get(key), thread, scope));
            }
            return compound;
        }
        if (object instanceof Iterable) {
            Iterator iterator = ((Iterable)object).iterator();
            NBTTagList list = new NBTTagList();
            while(iterator.hasNext()){
                list.add(convert(iterator.next(), thread, scope));
            }
            return list;
        }
        throw nextRule;
	}

    @Override
    public Class<NBTBase> getClassTo() {
        return NBTBase.class;
    }
}
