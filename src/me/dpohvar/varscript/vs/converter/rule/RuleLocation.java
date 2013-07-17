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
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleLocation extends ConvertRule<Location>{

    public RuleLocation() {
        super(10);
    }

    @Override
    public <V> Location convert(V object, VSThread thread,VSScope scope) throws NextRule {
        if (object==null) return thread.getProgram().getCaller().getLocation();
        if (object instanceof Number) return thread.getProgram().getCaller().getLocation().add(0.0,((Number)object).doubleValue(),0);
        if (object instanceof Character) return thread.getProgram().getCaller().getLocation().add(0.0,((Character)object),0);
        if (object instanceof String) return Bukkit.getPlayer((String)object).getLocation();
        if (object instanceof Entity) return ((Entity)object).getLocation();
        if (object instanceof Vector) return thread.getProgram().getCaller().getLocation().add((Vector)object);
        if (object instanceof Block) return ((Block)object).getLocation();
        if (object instanceof BlockState) return ((BlockState)object).getLocation();
        if (object instanceof Inventory) {
            Object holder = ((Inventory)object).getHolder();
            return convert(holder,thread,scope);
        }
        if (object instanceof Region) return ((Region)object).getCenter();
        if (object instanceof World) return ((World)object).getSpawnLocation();
        throw nextRule;
		// в идеале написать в конце Throw nextRule вместо return true
		
		// класс Number объединяет в себе все числовые типы: Byte,Short,Int,Long,Float,Double и другие (хз есть ли там Character)
		// делаем конвертер для каждого из них
		// а также для Vector,Entity,Player,Inventory,Collection,List,Map,Region и других.
		// в List мы конвертим инвентарь, коллекцию, Inventory, Строку и т.д.
    }

    @Override
    public Class<Location> getClassTo() {
        return Location.class;
    }
}
