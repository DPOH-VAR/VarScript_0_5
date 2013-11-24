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
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitInventory {

    static Class<?> classItemStack = ReflectBukkitUtils.getClass("<nms>ItemStack", "net.minecraft.item.ItemStack");
    static Class<?> classCraftItemStack = ReflectBukkitUtils.getClass("<cb>inventory.CraftItemStack", "<cb>inventory.CraftItemStack");

    public static ItemStack newItem(int id, int amount, int dmg) {
        Object m = ReflectUtils.callConstructor(
                classItemStack,
                new Class[]{int.class, int.class, int.class},
                id, amount, dmg
        );
        return (ItemStack) ReflectUtils.callConstructor(classCraftItemStack, new Class[]{classItemStack}, m);
    }

    public static void load() {

        VSCompiler.addRule(new SimpleCompileRule(
                "INVENTORY",
                "INVENTORY INV",
                "Object",
                "Inventory",
                "inventory",
                "get inventory of some",
                new SimpleWorker(new int[]{0xB0}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
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
                        Inventory inv = v.peek(Inventory.class);
                        HashMap<Integer, ItemStack> fit = inv.addItem(val);
                        if (!fit.isEmpty()) {
                            Location l = null;
                            InventoryHolder h = inv.getHolder();
                            if (h instanceof Entity) {
                                l = ((Entity) h).getLocation();
                            } else if (h instanceof BlockState) {
                                l = ((BlockState) h).getLocation();
                            }
                            if (l != null) for (ItemStack item : fit.values()) {
                                l.getWorld().dropItem(l, item);
                            }
                        }
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
                                newItem(id, amount, data)
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
                        for (ItemStack item : v.pop(Inventory.class)) items.add(item);
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
                        v.push(inv.containsAtLeast(item, 1));
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
                        Inventory inv = v.peek(Inventory.class);
                        inv.removeItem(item);
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
                        v.push(inv.containsAtLeast(item, amt));
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
                        v.push(inv.contains(item, amt));
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "HELMET",
                "HELMET HLM",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get item in helmet slot",
                new SimpleWorker(new int[]{0xBF, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getHelmet()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CHESTPLATE",
                "CHESTPLATE CHP",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get item in chesplate slot",
                new SimpleWorker(new int[]{0xBF, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getChestplate()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LEGGINGS",
                "LEGGINGS LEG",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get item in leggings slot",
                new SimpleWorker(new int[]{0xBF, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getLeggings()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BOOTS",
                "BOOTS",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get item in boots slot",
                new SimpleWorker(new int[]{0xBF, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getBoots()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HAND",
                "HAND",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get item in hand",
                new SimpleWorker(new int[]{0xBF, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getItemInHand()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETHELMET",
                "SETHELMET SETHLM >HLM",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set item in helmet slot",
                new SimpleWorker(new int[]{0xBF, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(PlayerInventory.class).setHelmet(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCHESTPLATE",
                "SETCHESTPLATE SETCHP >CHP",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set item in chestplate slot",
                new SimpleWorker(new int[]{0xBF, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(PlayerInventory.class).setChestplate(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLEGGINGS",
                "SETLEGGINGS SETLEG >LEG",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set item in leggings slot",
                new SimpleWorker(new int[]{0xBF, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(PlayerInventory.class).setLeggings(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBOOTS",
                "SETBOOTS >BOOTS",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set item in boots slot",
                new SimpleWorker(new int[]{0xBF, 0x0B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(PlayerInventory.class).setBoots(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETHAND",
                "SETHAND >HAND",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set item in hand slot",
                new SimpleWorker(new int[]{0xBF, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        v.peek(PlayerInventory.class).setItemInHand(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ARMOR",
                "ARMOR",
                "PlayerInventory",
                "List(ItemStack)",
                "inventory",
                "get all armor contents",
                new SimpleWorker(new int[]{0xBF, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                Arrays.asList(
                                        v.pop(PlayerInventory.class).getArmorContents()
                                )
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETARMOR",
                "SETARMOR >ARMOR",
                "PlayerInventory List(ItemStack)",
                "PlayerInventory",
                "inventory",
                "set armor contents to player",
                new SimpleWorker(new int[]{0xBF, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        List val = v.pop(List.class);
                        ItemStack[] items = new ItemStack[val.size()];
                        int i = 0;
                        for (Object t : val) items[i++] = v.convert(ItemStack.class, t);
                        v.peek(PlayerInventory.class).setArmorContents(items);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HANDSLOT",
                "HANDSLOT",
                "PlayerInventory",
                "ItemStack",
                "inventory",
                "get held item slot",
                new SimpleWorker(new int[]{0xBF, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(PlayerInventory.class).getHeldItemSlot()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETHANDSLOT",
                "SETHANDSLOT >HANDSLOT",
                "PlayerInventory ItemStack",
                "PlayerInventory",
                "inventory",
                "set mew held item slot for player",
                new SimpleWorker(new int[]{0xBF, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.peek(PlayerInventory.class).setHeldItemSlot(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALL",
                "SETALL",
                "Inventory List(ItemStack)",
                "Inventory",
                "inventory",
                "set all items in inventory",
                new SimpleWorker(new int[]{0xBF, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        List list = v.pop(List.class);
                        Inventory inv = v.peek(Inventory.class);
                        ItemStack[] items = new ItemStack[list.size()];
                        int i = 0;
                        for (Object o : list) items[i++] = v.convert(ItemStack.class, o);
                        inv.setContents(items);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SET",
                "SET ISET",
                "Inventory Integer(slot) ItemStack",
                "Inventory",
                "inventory",
                "set item to slot in inventory",
                new SimpleWorker(new int[]{0xBF, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Integer slot = v.pop(Integer.class);
                        v.peek(Inventory.class).setItem(slot, item);
                    }
                }
        ));


    }


}
