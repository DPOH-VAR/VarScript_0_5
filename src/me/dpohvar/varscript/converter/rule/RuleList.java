package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.region.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleList extends ConvertRule<List>{

    public RuleList() {
        super(10);
    }

    @Override
    public <V> List convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof Number
                ||object instanceof Entity
                ||object instanceof Character
                ||object instanceof Location
                ||object instanceof Vector
                ||object instanceof Block
                ||object instanceof BlockState
                ||object instanceof ItemStack
                ||object instanceof PotionEffect
                ) {
            ArrayList a = new ArrayList();
            a.add(object);
            return a;
        }
        if (object instanceof String) {
            ArrayList a = new ArrayList();
            for(char c:((String) object).toCharArray()) a.add(c);
            return a;
        }
        if (object instanceof Inventory) {
            ArrayList a = new ArrayList();
            for(ItemStack i:(Inventory)object) a.add(i);
            return a;
        }
        if (object instanceof Fieldable) {
            return new ArrayList(((Fieldable)object).getAllFields());
        }
        if (object instanceof Object[]) return Arrays.asList((Object[]) object);
        if (object instanceof Collection) return new ArrayList((Collection)object);
        if (object instanceof Map) return new ArrayList(((Map)object).keySet());
        if (object instanceof Region) return new ArrayList(((Region)object).getBlocks());
        if (object instanceof World) return ((World)object).getEntities();
        if (object instanceof NBTTagList) return convert(((NBTTagList)object).asList(),thread,scope);
        if (object instanceof NBTTagCompound) return convert(((NBTTagCompound)object).asList(),thread,scope);
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            ArrayList<Byte> a = new ArrayList<Byte>();
            for(byte b:bytes)a.add(b);
            return a;
        }
        if (object instanceof int[]) {
            int[] ints = (int[])object;
            ArrayList<Integer> a = new ArrayList<Integer>();
            for(int b:ints)a.add(b);
            return a;
        }
        throw nextRule;
    }

    @Override
    public Class<List> getClassTo() {
        return List.class;
    }
}
