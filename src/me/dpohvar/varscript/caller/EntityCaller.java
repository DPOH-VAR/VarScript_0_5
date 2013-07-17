package me.dpohvar.varscript.caller;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class EntityCaller extends Caller {

    protected Entity entity;

    public EntityCaller(Entity entity){
        this.entity = entity;
    }

    @Override
    public Entity getInstance() {
        return entity;
    }

    @Override
    public Location getLocation(){
        return entity.getLocation();
    }
}
