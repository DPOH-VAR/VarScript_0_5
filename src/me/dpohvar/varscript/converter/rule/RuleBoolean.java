package me.dpohvar.varscript.converter.rule;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleBoolean extends ConvertRule<Boolean>{

    public RuleBoolean() {
        super(10);
    }

    @Override
    public <V> Boolean convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return false;
        if (object instanceof Number) return !object.equals(0);
        if (object instanceof Character) return !object.equals(0);
        if (object instanceof String) return !((String)object).isEmpty();
        if (object instanceof Entity) return !((Entity)object).isDead();
        if (object instanceof Vector) return ((Vector)object).length()!=0;
        if (object instanceof Block) return !((Block)object).isEmpty();
        if (object instanceof BlockState) return ((BlockState)object).getTypeId()!=0;
        if (object instanceof Inventory) {
            for(ItemStack a:(Inventory)object) if (a!=null) return true;
            return false;
        }
        if (object instanceof ItemStack) return ((ItemStack)object).getAmount()!=0;
        if (object instanceof Collection) return !((Collection)object).isEmpty();
        if (object instanceof OfflinePlayer) return ((OfflinePlayer)object).isOnline();
        if (object instanceof Program) return ((Program)object).isFinished();
        if (object instanceof Map) return !((Map)object).isEmpty();
        if (object instanceof byte[]) {
            return ((byte[])object).length!=0;
        }
        return true;
		// в идеале написать в конце Throw nextRule вместо return true
		
		// класс Number объединяет в себе все числовые типы: Byte,Short,Int,Long,Float,Double и другие (хз есть ли там Character)
		// делаем конвертер для каждого из них
		// а также для Vector,Entity,Player,Inventory,Collection,List,Map,Region и других.
		// в List мы конвертим инвентарь, коллекцию, Inventory, Строку и т.д.
    }

    @Override
    public Class<Boolean> getClassTo() {
        return Boolean.class;
    }
}
