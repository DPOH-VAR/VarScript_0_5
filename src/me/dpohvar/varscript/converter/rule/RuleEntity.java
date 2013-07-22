package me.dpohvar.varscript.converter.rule;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleEntity extends ConvertRule<Entity>{

    public RuleEntity() {
        super(10);
    }

    @Override
    public <V> Entity convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof Number) {
            int id = ((Number) object).intValue();
            for(World w:Bukkit.getWorlds()){
                for(Entity e:w.getEntities()){
                    if(e.getEntityId()==id) return e;
                }
            }
        }
        if (object instanceof String) return Bukkit.getPlayer((String)object);
        if (object instanceof Inventory) return convert(((Inventory)object).getHolder(),thread,scope);
        if (object instanceof OfflinePlayer) return ((OfflinePlayer)object).getPlayer();
        if (object instanceof Program) return convert(((Program)object).getCaller().getInstance(),thread,scope);

        throw nextRule;
    }

    @Override
    public Class<Entity> getClassTo() {
        return Entity.class;
    }
}
