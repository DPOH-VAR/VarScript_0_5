package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.reflect.ReflectBukkitUtils;
import me.dpohvar.varscript.utils.reflect.ReflectUtils;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleItemStack extends ConvertRule<ItemStack> {

    static Class<?> classItemStack = ReflectBukkitUtils.getMinecraftClass("ItemStack");
    static Class<?> classCraftItemStack = ReflectBukkitUtils.getBukkitClass("inventory.CraftItemStack");

    public static ItemStack newItem(int id, int amount, int dmg) {
        Object m = ReflectUtils.callConstructor(
                classItemStack,
                new Class[]{int.class, int.class, int.class},
                id, amount, dmg
        );
        return (ItemStack) ReflectUtils.callConstructor(classCraftItemStack, new Class[]{classItemStack}, m);
    }

    public RuleItemStack() {
        super(10);
    }

    @Override
    public <V> ItemStack convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Number) return newItem(((Number) object).intValue(), 1, 0);
        if (object instanceof String) return Bukkit.getPlayer(((String) object).trim()).getItemInHand();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        if (object instanceof Inventory) return convert(((Inventory) object).getHolder(), thread, scope);
        if (object instanceof OfflinePlayer) return ((OfflinePlayer) object).getPlayer().getItemInHand();
        if (object instanceof Block) {
            Block b = (Block) object;
            return newItem(b.getTypeId(), 1, b.getData());
        }
        if (object instanceof Location) {
            Block b = ((Location) object).getBlock();
            return newItem(b.getTypeId(), 1, b.getData());
        }

        throw nextRule;
    }

    @Override
    public Class<ItemStack> getClassTo() {
        return ItemStack.class;
    }
}
