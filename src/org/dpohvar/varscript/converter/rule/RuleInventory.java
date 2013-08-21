package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleInventory extends ConvertRule<Inventory> {

    public RuleInventory() {
        super(10);
    }

    @Override
    public <V> Inventory convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof InventoryHolder) return ((InventoryHolder) object).getInventory();
        if (object instanceof String) return Bukkit.getPlayer(((String) object).trim()).getInventory();
        if (object instanceof Block) return ((InventoryHolder) ((Block) object).getState()).getInventory();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<Inventory> getClassTo() {
        return Inventory.class;
    }
}
