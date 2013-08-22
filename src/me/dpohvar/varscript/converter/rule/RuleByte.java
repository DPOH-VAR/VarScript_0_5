package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.powernbt.nbt.NBTTagList;
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

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleByte extends ConvertRule<Byte> {

    public RuleByte() {
        super(10);
    }

    @Override
    public <V> Byte convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return 0;
        if (object instanceof Number) return ((Number) object).byteValue();
        if (object instanceof Character) return (byte) ((Character) object).charValue();
        if (object instanceof Location) return (byte) ((Location) object).getY();
        if (object instanceof String) return new Byte((String) object);
        if (object instanceof Entity) return (byte) ((Entity) object).getEntityId();
        if (object instanceof Vector) return (byte) ((Vector) object).length();
        if (object instanceof Block) return (byte) ((Block) object).getTypeId();
        if (object instanceof BlockState) return (byte) ((BlockState) object).getTypeId();
        if (object instanceof Inventory) return (byte) ((Inventory) object).getSize();
        if (object instanceof ItemStack) return (byte) ((ItemStack) object).getTypeId();
        if (object instanceof Collection) return (byte) ((Collection) object).size();
        if (object instanceof Map) return (byte) ((Map) object).size();
        if (object instanceof Boolean) return ((Boolean) object) ? (byte) 1 : (byte) 0;
        if (object instanceof Region) return (byte) ((Region) object).getBlocks().size();
        if (object instanceof World) return (byte) Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (byte) ((PotionEffect) object).getType().getId();
        try {
            if (object instanceof Score) return (byte) ((Score) object).getScore();
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            if (object instanceof NBTTagList) return (byte) ((NBTTagList) object).size();
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
            if (object instanceof NBTTagCompound) return (byte) ((NBTTagCompound) object).size();
        } catch (NoClassDefFoundError ignored) {
        }
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length == 0) return 0;
            return bytes[0];
        }
        throw nextRule;
    }

    @Override
    public Class<Byte> getClassTo() {
        return Byte.class;
    }
}
