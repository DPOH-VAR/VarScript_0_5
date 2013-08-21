package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.powernbt.nbt.NBTTagList;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.Program;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;
import org.dpohvar.varscript.vs.Thread;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleBoolean extends ConvertRule<Boolean> {

    public RuleBoolean() {
        super(10);
    }

    @Override
    public <V> Boolean convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return false;
        if (object instanceof Byte) return !object.equals((byte) 0);
        if (object instanceof Short) return !object.equals((short) 0);
        if (object instanceof Integer) return !object.equals(0);
        if (object instanceof Long) return !object.equals((long) 0);
        if (object instanceof Float) return !object.equals((float) 0);
        if (object instanceof Double) return !object.equals((double) 0);
        if (object instanceof Character) return !object.equals(0);
        if (object instanceof String) return !((String) object).isEmpty();
        if (object instanceof Entity) return !((Entity) object).isDead();
        if (object instanceof Vector) return ((Vector) object).length() != 0;
        if (object instanceof Block) return !((Block) object).isEmpty();
        if (object instanceof BlockState) return ((BlockState) object).getTypeId() != 0;
        if (object instanceof Inventory) {
            for (ItemStack a : (Inventory) object) if (a != null) return true;
            return false;
        }
        if (object instanceof ItemStack) return ((ItemStack) object).getAmount() != 0;
        if (object instanceof Collection) return !((Collection) object).isEmpty();
        if (object instanceof OfflinePlayer) return ((OfflinePlayer) object).isOnline();
        if (object instanceof Program) return !((Program) object).isFinished();
        if (object instanceof Thread) return !((Thread) object).isFinished();
        if (object instanceof Map) return !((Map) object).isEmpty();
        if (object instanceof int[]) return ((int[]) object).length != 0;
        if (object instanceof Object[]) return ((Object[]) object).length != 0;
        if (object instanceof byte[]) return ((byte[]) object).length != 0;
        try {
            if (object instanceof NBTTagCompound) return ((NBTTagCompound) object).size() > 0;
            if (object instanceof NBTTagList) return ((NBTTagList) object).size() > 0;
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            if (object instanceof Score) return ((Score) object).getScore() != 0;
        } catch (NoClassDefFoundError ignored) {
        }
        return true;
    }

    @Override
    public Class<Boolean> getClassTo() {
        return Boolean.class;
    }
}
