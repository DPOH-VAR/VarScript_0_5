package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.powernbt.nbt.NBTTagList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.utils.region.Region;
import org.dpohvar.varscript.vs.Fieldable;
import org.dpohvar.varscript.vs.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleIterable extends ConvertRule<Iterable> {

    public RuleIterable() {
        super(10);
    }

    @Override
    public <V> Iterable convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Number
                || object instanceof Entity
                || object instanceof Character
                || object instanceof Location
                || object instanceof Vector
                || object instanceof Block
                || object instanceof BlockState
                || object instanceof ItemStack
                || object instanceof PotionEffect
                ) {
            ArrayList a = new ArrayList();
            a.add(object);
            return a;
        }
        if (object instanceof String) {
            ArrayList a = new ArrayList();
            for (char c : ((String) object).toCharArray()) a.add(c);
            return a;
        }
        if (object instanceof Inventory) {
            ArrayList a = new ArrayList();
            for (ItemStack i : (Inventory) object) a.add(i);
            return a;
        }
        if (object instanceof Fieldable) {
            return new ArrayList(((Fieldable) object).getAllFields());
        }
        if (object instanceof Object[]) return Arrays.asList((Object[]) object);
        if (object instanceof Collection) return new ArrayList((Collection) object);
        if (object instanceof Map) return new ArrayList(((Map) object).keySet());
        if (object instanceof Region) return new ArrayList(((Region) object).getBlocks());
        if (object instanceof World) return ((World) object).getEntities();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            ArrayList<Byte> a = new ArrayList<Byte>();
            for (byte b : bytes) a.add(b);
            return a;
        }
        if (object instanceof int[]) {
            int[] ints = (int[]) object;
            ArrayList<Integer> a = new ArrayList<Integer>();
            for (int b : ints) a.add(b);
            return a;
        }
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
            if (object instanceof NBTTagList) return ((NBTTagList) object).asList();
            if (object instanceof NBTTagCompound) return ((NBTTagCompound) object).asList();
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<Iterable> getClassTo() {
        return Iterable.class;
    }
}
