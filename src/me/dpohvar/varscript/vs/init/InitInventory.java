package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.utils.reflect.ReflectBukkitUtils;
import me.dpohvar.varscript.utils.reflect.ReflectUtils;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitInventory {

    static Class<?> classItemStack = ReflectBukkitUtils.getMinecraftClass("ItemStack");
    static Class<?> classCraftItemStack = ReflectBukkitUtils.getBukkitClass("inventory.CraftItemStack");

    public static ItemStack newItem(int id,int amount,int dmg){
        Object m = ReflectUtils.callConstructor(
                classItemStack,
                new Class[]{int.class, int.class, int.class},
                id, amount, dmg
        );
        return (ItemStack) ReflectUtils.callConstructor(classCraftItemStack,new Class[]{classItemStack},m);
    }

    public static void load(){

        VSCompiler.addRule(new SimpleCompileRule(
                "INVENTORY",
                "INVENTORY INV",
                "Object",
                "Inventory",
                "inventory",
                "get inventory of some",
                new SimpleWorker(new int[]{0xB0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(Inventory.class));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CLEAR",
                "CLEAR",
                "Inventory",
                "Inventory",
                "inventory",
                "clear inventory",
                new SimpleWorker(new int[]{0xB1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Inventory.class).clear();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ID",
                "ID",
                "ItemStack",
                "Integer",
                "item",
                "get item id",
                new SimpleWorker(new int[]{0xB2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(ItemStack.class).getTypeId()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DATA",
                "DATA",
                "ItemStack",
                "Short",
                "item",
                "get item data or durability",
                new SimpleWorker(new int[]{0xB3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(ItemStack.class).getDurability()
                        );
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "AMOUNT",
                "AMOUNT AMT",
                "ItemStack",
                "Byte",
                "item",
                "get amount of item stack",
                new SimpleWorker(new int[]{0xB4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(ItemStack.class).getAmount()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETID",
                "SETID >ID",
                "ItemStack Integer",
                "ItemStack",
                "item",
                "set item id",
                new SimpleWorker(new int[]{0xB5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.peek(ItemStack.class).setTypeId(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETDATA",
                "SETDATA >DATA",
                "ItemStack Short",
                "ItemStack",
                "item",
                "set item data or durability",
                new SimpleWorker(new int[]{0xB6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Short val = v.pop(Short.class);
                        v.peek(ItemStack.class).setDurability(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETAMOUNT",
                "SETAMOUNT SETAMT >AMT",
                "ItemStack Integer",
                "ItemStack",
                "item",
                "set amount of item stack",
                new SimpleWorker(new int[]{0xB7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.peek(ItemStack.class).setAmount(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TAKE",
                "TAKE",
                "Inventory ItemStack",
                "Inventory",
                "item inventory",
                "take item to inventory",
                new SimpleWorker(new int[]{0xB8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(Inventory.class).addItem(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ITEM",
                "ITEM",
                "Integer(id) Short(data) Integer(amount)",
                "ItemStack",
                "item",
                "create new item stack with id,data,amount",
                new SimpleWorker(new int[]{0xB9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer amount = v.pop(Integer.class);
                        Short data = v.pop(Short.class);
                        Integer id = v.pop(Integer.class);
                        v.push(
                                newItem(id,amount,data)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "GET",
                "GET",
                "Inventory Integer(position)",
                "ItemStack",
                "inventory item",
                "get item from inventory in position",
                new SimpleWorker(new int[]{0xBA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.push(
                                v.pop(Inventory.class).getItem(val)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "GETALL",
                "GETALL",
                "Inventory",
                "List(ItemStack)",
                "inventory item",
                "get all items from inventory",
                new SimpleWorker(new int[]{0xBB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                        for(ItemStack item:v.pop(Inventory.class)) items.add(item);
                        v.push(items);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASITEM",
                "HASITEM IHAS",
                "Inventory(A) ItemStack(B)",
                "Boolean",
                "inventory item",
                "true, if inventory A has item B",
                new SimpleWorker(new int[]{0xBC}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Inventory inv = v.pop(Inventory.class);
                        v.push(inv.contains(item));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REMITEM",
                "REMITEM IREM",
                "Inventory(A) ItemStack(B)",
                "Inventory(A)",
                "inventory item",
                "remove item B from inventory A",
                new SimpleWorker(new int[]{0xBD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Inventory inv = v.pop(Inventory.class);
                        inv.remove(item);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REMITEMID",
                "REMITEMID IREMID",
                "Inventory(A) Integer(B)",
                "Inventory(A)",
                "inventory item",
                "remove item with id B from inventory A",
                new SimpleWorker(new int[]{0xBE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer item = v.pop(Integer.class);
                        Inventory inv = v.pop(Inventory.class);
                        inv.remove(item);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASITEMS",
                "HASITEMS",
                "Inventory(A) ItemStack(B) Integer(C)",
                "Boolean",
                "inventory item",
                "true, if inventory A has items B in amount C",
                new SimpleWorker(new int[]{0xBF, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer amt = v.pop(Integer.class);
                        ItemStack item = v.pop(ItemStack.class);
                        Inventory inv = v.pop(Inventory.class);
                        v.push(inv.contains(item,amt));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASITEMID",
                "HASITEMID HASID",
                "Inventory(A) Integer(B)",
                "Boolean",
                "inventory item",
                "true, if inventory A has items with id B",
                new SimpleWorker(new int[]{0xBF, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer id = v.pop(Integer.class);
                        Inventory inv = v.pop(Inventory.class);
                        v.push(inv.contains(id));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASITEMIDS",
                "HASITEMIDS",
                "Inventory(A) Integer(ID) Integer(AMT)",
                "Boolean",
                "inventory item",
                "true, if inventory A has items with ID in amount AMT",
                new SimpleWorker(new int[]{0xBF, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer amt = v.pop(Integer.class);
                        Integer item = v.pop(Integer.class);
                        Inventory inv = v.pop(Inventory.class);
                        v.push(inv.contains(item,amt));
                    }
                }
        ));


    }


}
