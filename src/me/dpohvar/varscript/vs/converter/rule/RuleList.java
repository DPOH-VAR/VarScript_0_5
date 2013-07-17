package me.dpohvar.varscript.vs.converter.rule;

import me.dpohvar.varscript.vs.VSFieldable;
import me.dpohvar.varscript.vs.VSScope;
import me.dpohvar.varscript.vs.VSSimpleScope;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.converter.NextRule;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public <V> List convert(V object, VSThread thread,VSScope scope) throws NextRule {
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
        if (object instanceof VSFieldable) {
            return new ArrayList(((VSFieldable)object).getAllFields());
        }
        if (object instanceof Collection) return new ArrayList((Collection)object);
        if (object instanceof Map) return new ArrayList(((Map)object).keySet());
        if (object instanceof Region) return new ArrayList(((Region)object).getBlocks());
        if (object instanceof World) return ((World)object).getEntities();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            ArrayList<Byte> a = new ArrayList<Byte>();
            for(byte b:bytes)a.add(b);
            return a;
        }
        throw nextRule;
		// в идеале написать в конце Throw nextRule вместо return true
		
		// класс Number объединяет в себе все числовые типы: Byte,Short,Int,Long,Float,Double и другие (хз есть ли там Character)
		// делаем конвертер для каждого из них
		// а также для Vector,Entity,Player,Inventory,Collection,List,Map,Region и других.
		// в List мы конвертим инвентарь, коллекцию, Inventory, Строку и т.д.
    }

    @Override
    public Class<List> getClassTo() {
        return List.class;
    }
}
