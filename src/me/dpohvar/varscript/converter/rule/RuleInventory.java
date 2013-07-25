package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleInventory extends ConvertRule<Inventory>{

    public RuleInventory() {
        super(10);
    }

    @Override
    public <V> Inventory convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof InventoryHolder) return ((InventoryHolder)object).getInventory();
        if (object instanceof String) return Bukkit.getPlayer((String)object).getInventory();
        if (object instanceof Block) return ((InventoryHolder)((Block)object).getState()).getInventory();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        throw nextRule;
    }

    @Override
    public Class<Inventory> getClassTo() {
        return Inventory.class;
    }
}
