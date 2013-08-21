package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.utils.reflect.NBTTagWrapper;
import org.dpohvar.varscript.vs.CommandList;
import org.dpohvar.varscript.vs.Scope;

import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleNBTTag extends ConvertRule<NBTBase> {

    public RuleNBTTag() {
        super(10);
    }

    @Override
    public <V> NBTBase convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof NBTTagWrapper) return ((NBTTagWrapper) object).getTag();
        if (object instanceof NBTContainer) return ((NBTContainer) object).getTag();
        if (object instanceof Number) return NBTBase.getByValue(object);
        if (object instanceof int[]) return new NBTTagIntArray((int[]) object);
        if (object instanceof byte[]) return new NBTTagByteArray((byte[]) object);
        if (object instanceof String) return new NBTTagString((String) object);
        if (object instanceof CommandList) return new NBTTagByteArray(((CommandList) object).toBytes());
        if (object instanceof Block) return new NBTContainerBlock((Block) object).getTag();
        if (object instanceof BlockState) return new NBTContainerBlock(((BlockState) object).getBlock()).getTag();
        if (object instanceof Location) return new NBTContainerBlock(((Location) object).getBlock()).getTag();
        if (object instanceof Entity) return new NBTContainerEntity((Entity) object).getTag();
        if (object instanceof ItemStack) return new NBTContainerItem((ItemStack) object).getTag();
        if (object instanceof Map) {
            Map map = (Map) object;
            NBTTagCompound compound = new NBTTagCompound();
            for (Object key : map.keySet()) {
                compound.set(key.toString(), convert(map.get(key), thread, scope));
            }
            return compound;
        }
        if (object instanceof Iterable) {
            Iterator iterator = ((Iterable) object).iterator();
            NBTTagList list = new NBTTagList();
            while (iterator.hasNext()) {
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
