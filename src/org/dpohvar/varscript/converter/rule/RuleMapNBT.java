package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTContainerBlock;
import me.dpohvar.powernbt.nbt.NBTContainerEntity;
import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleMapNBT extends ConvertRule<Map> {

    public RuleMapNBT() {
        super(8);
    }

    @Override
    public <V> Map convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object instanceof Block) return new NBTContainerBlock((Block) object).getTag().asMap();
        if (object instanceof BlockState)
            return new NBTContainerBlock(((BlockState) object).getBlock()).getTag().asMap();
        if (object instanceof Location) return new NBTContainerBlock(((Location) object).getBlock()).getTag().asMap();
        if (object instanceof Entity) return new NBTContainerEntity((Entity) object).getTag().asMap();
        if (object instanceof ItemStack) return new NBTContainerItem((ItemStack) object).getTag().asMap();
        if (object instanceof NBTTagCompound) return ((NBTTagCompound) object).asMap();
        throw nextRule;
    }

    @Override
    public Class<Map> getClassTo() {
        return Map.class;
    }
}
