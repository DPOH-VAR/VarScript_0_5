package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleWorld extends ConvertRule<World> {

    public RuleWorld() {
        super(10);
    }

    @Override
    public <V> World convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return thread.getProgram().getCaller().getLocation().getWorld();
        if (object instanceof Number) return Bukkit.getWorlds().get(((Number) object).intValue());
        if (object instanceof String) return Bukkit.getWorld((String) object);
        if (object instanceof Entity) return ((Entity) object).getWorld();
        if (object instanceof Block) return ((Block) object).getWorld();
        if (object instanceof BlockState) return ((BlockState) object).getWorld();
        if (object instanceof Inventory) return convert(((Inventory) object).getHolder(), thread, scope);
        if (object instanceof OfflinePlayer)
            return convert(((OfflinePlayer) object).getPlayer().getLocation(), thread, scope);
        if (object instanceof Location) return ((Location) object).getWorld();
        if (object instanceof Vector) return thread.getProgram().getCaller().getLocation().getWorld();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }

        throw nextRule;
    }

    @Override
    public Class<World> getClassTo() {
        return World.class;
    }
}
