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
public class RuleDouble extends ConvertRule<Double> {

    public RuleDouble() {
        super(10);
    }

    @Override
    public <V> Double convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return 0.0;
        if (object instanceof Number) return ((Number) object).doubleValue();
        if (object instanceof Location) return ((Location) object).getY();
        if (object instanceof Character) return (double) ((Character) object).charValue();
        if (object instanceof String) return new Double((String) object);
        if (object instanceof Enum) return (double) ((Enum) object).ordinal();
        if (object instanceof Entity) return (double) ((Entity) object).getEntityId();
        if (object instanceof Vector) return ((Vector) object).length();
        if (object instanceof Block) return (double) ((Block) object).getTypeId();
        if (object instanceof BlockState) return (double) ((BlockState) object).getTypeId();
        if (object instanceof Inventory) return (double) ((Inventory) object).getSize();
        if (object instanceof ItemStack) return (double) ((ItemStack) object).getTypeId();
        if (object instanceof Collection) return (double) ((Collection) object).size();
        if (object instanceof Map) return (double) ((Map) object).size();
        if (object instanceof Boolean) return ((Boolean) object) ? 1.0 : 0.0;
        if (object instanceof Region) return (double) ((Region) object).getBlocks().size();
        if (object instanceof World) return (double) Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (double) ((PotionEffect) object).getType().getId();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            if (object instanceof Score) return (double) ((Score) object).getScore();
        } catch (NoClassDefFoundError ignored) {
        }
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length == 0) return 0.0;
            return ByteBuffer.wrap(bytes).getDouble();
        }
        throw nextRule;
    }

    @Override
    public Class<Double> getClassTo() {
        return Double.class;
    }
}
