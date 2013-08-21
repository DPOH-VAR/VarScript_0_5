package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleNBTContainer extends ConvertRule<NBTContainer> {

    public RuleNBTContainer() {
        super(10);
    }

    @Override
    public <V> NBTContainer convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Block) return new NBTContainerBlock((Block) object);
        if (object instanceof BlockState) return new NBTContainerBlock(((BlockState) object).getBlock());
        if (object instanceof Location) return new NBTContainerBlock(((Location) object).getBlock());
        if (object instanceof Entity) return new NBTContainerEntity((Entity) object);
        if (object instanceof ItemStack) return new NBTContainerItem((ItemStack) object);
        if (object instanceof String) return new NBTContainerFile(new File((String) object));
        throw nextRule;
    }

    @Override
    public Class<NBTContainer> getClassTo() {
        return NBTContainer.class;
    }
}
