package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.AIR;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleItemStack extends ConvertRule<ItemStack>{

    public RuleItemStack() {
        super(10);
    }

    @Override
    public <V> ItemStack convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return new ItemStack(AIR);
        if (object instanceof Number) return new ItemStack(((Number)object).intValue());
        if (object instanceof String) return Bukkit.getPlayer(object.toString()).getItemInHand();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        if (object instanceof Inventory) return convert(((Inventory)object).getHolder(),thread,scope);
        if (object instanceof OfflinePlayer) return ((OfflinePlayer)object).getPlayer().getItemInHand();
        if (object instanceof Block) {
            Block b = (Block) object;
            return new ItemStack(b.getTypeId(),1,b.getData());
        }
        if (object instanceof Location) {
            Block b = ((Location)object).getBlock();
            return new ItemStack(b.getTypeId(),1,b.getData());
        }

        throw nextRule;
    }

    @Override
    public Class<ItemStack> getClassTo() {
        return ItemStack.class;
    }
}
