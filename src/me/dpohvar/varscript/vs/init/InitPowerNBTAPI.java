package me.dpohvar.varscript.vs.init;

import me.dpohvar.powernbt.nbt.*;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */

public class InitPowerNBTAPI {
    public static void load(){
        if(Bukkit.getPluginManager().getPlugin("PowerNBT")==null) return;

        VSCompiler.addRule(new SimpleCompileRule(
                "NBT",
                "NBT",
                "NBTContainer(A)",
                "NBTBase",
                "nbt",
                "get nbt value of object\nExample: ME NBT",
                new SimpleWorker(new int[]{0xC0}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        NBTContainer c = v.pop(NBTContainer.class);
                        v.push(c.getTag());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETNBT",
                "SETNBT >NBT",
                "NBTContainer(A) NBTBase",
                "NBTContainer(A)",
                "nbt",
                "save nbt value to conrainer\nExample: ME NBT 10 >>healF SETNBT",
                new SimpleWorker(new int[]{0xC1}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        NBTBase b = v.pop(NBTBase.class);
                        NBTContainer c = v.peek(NBTContainer.class);
                        c.setCustomTag(b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTBLOCK",
                "NBTBLOCK",
                "Block",
                "NBTContainer",
                "nbt block",
                "put to stack new nbt container with block",
                new SimpleWorker(new int[]{0xC2}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerBlock(v.pop(Block.class)));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTENTITY",
                "NBTENTITY",
                "Entity",
                "NBTContainer",
                "nbt entity",
                "put to stack new nbt container with entity",
                new SimpleWorker(new int[]{0xC3}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerEntity(v.pop(Entity.class)));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTFILE",
                "NBTFILE",
                "String",
                "NBTContainer",
                "nbt",
                "put to stack new nbt container with file",
                new SimpleWorker(new int[]{0xC4}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerFile(new File(v.pop(String.class))));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTGZIP",
                "NBTGZIP",
                "String",
                "NBTContainer",
                "nbt",
                "put to stack new nbt container with gzipped file",
                new SimpleWorker(new int[]{0xC5}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerFileGZip(new File(v.pop(String.class))));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTCUSTOM",
                "NBTCUSTOM",
                "String(name)",
                "NBTContainer",
                "nbt",
                "put to stack new nbt container with custom file ($name in PowerNBT)",
                new SimpleWorker(new int[]{0xC6}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerFileCustom(v.pop(String.class)));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NBTITEM",
                "NBTITEM",
                "ItemStack",
                "NBTContainer",
                "nbt",
                "put to stack new nbt container with item",
                new SimpleWorker(new int[]{0xC7}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new NBTContainerItem(v.pop(ItemStack.class)));
                    }
                }
        ));

    }
}
