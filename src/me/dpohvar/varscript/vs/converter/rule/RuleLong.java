package me.dpohvar.varscript.vs.converter.rule;

import me.dpohvar.varscript.vs.VSScope;
import me.dpohvar.varscript.vs.VSSimpleScope;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.converter.NextRule;
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

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleLong extends ConvertRule<Long>{

    public RuleLong() {
        super(10);
    }

    @Override
    public <V> Long convert(V object, VSThread thread,VSScope scope) throws NextRule {
        if (object==null) return 0L;
        if (object instanceof Number) return ((Number)object).longValue();
        if (object instanceof Character) return (long)((Character)object).charValue();
        if (object instanceof Location) return (long)((Location)object).getY();
        if (object instanceof String) return new Long((String) object);
        if (object instanceof Entity) return (long)((Entity)object).getEntityId();
        if (object instanceof Vector) return (long)((Vector)object).length();
        if (object instanceof Block) return (long)((Block)object).getTypeId();
        if (object instanceof BlockState) return (long)((BlockState)object).getTypeId();
        if (object instanceof Inventory) return (long)((Inventory)object).getSize();
        if (object instanceof ItemStack) return (long)((ItemStack)object).getTypeId();
        if (object instanceof Collection) return (long)((Collection)object).size();
        if (object instanceof Map) return (long)((Map)object).size();
        if (object instanceof Boolean) return ((Boolean)object)?1L:0L;
        if (object instanceof Region) return (long)((Region)object).getBlocks().size();
        if (object instanceof World) return (long)Bukkit.getWorlds().indexOf(object);
        if (object instanceof PotionEffect) return (long)((PotionEffect)object).getType().getId();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0l;
            return ByteBuffer.wrap(bytes).getLong();
        }
        throw nextRule;
		// в идеале написать в конце Throw nextRule вместо return true
		
		// класс Number объединяет в себе все числовые типы: Byte,Short,Int,Long,Float,Double и другие (хз есть ли там Character)
		// делаем конвертер для каждого из них
		// а также для Vector,Entity,Player,Inventory,Collection,List,Map,Region и других.
		// в List мы конвертим инвентарь, коллекцию, Inventory, Строку и т.д.
    }

    @Override
    public Class<Long> getClassTo() {
        return Long.class;
    }
}
