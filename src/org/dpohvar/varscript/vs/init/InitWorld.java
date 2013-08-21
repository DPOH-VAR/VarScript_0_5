package org.dpohvar.varscript.vs.init;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.vs.Context;
import org.dpohvar.varscript.vs.SimpleWorker;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.ThreadRunner;
import org.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitWorld {
    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "CANGENERATESTRUCTURES",
                "CANGENERATESTRUCTURES",
                "World",
                "Boolean",
                "world",
                "Returns true if world can generate structures",
                new SimpleWorker(new int[]{0xA0}) {
                    @Override
                    public void run(ThreadRunner r, org.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.canGenerateStructures());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EXPLODE",
                "EXPLODE EX",
                "Location Float(Power)",
                "Location",
                "world",
                "Create explosion",
                new SimpleWorker(new int[]{0xA1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float power = v.pop(Float.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().createExplosion(l, power);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FIREEXPLODE",
                "FIREEXPLODE FEX",
                "Location Float(Power)",
                "Location",
                "world",
                "Create explosion with fire",
                new SimpleWorker(new int[]{0xA2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float power = v.pop(Float.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().createExplosion(l, power, true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SAFEEXPLODE",
                "SAFEEXPLODE SEX",
                "Location Float(Power)",
                "Location",
                "world effect",
                "Create explosion without break blocks",
                new SimpleWorker(new int[]{0xA3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float power = v.pop(Float.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), power, false, false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SAFEFIREEXPLODE",
                "SAFEFIREEXPLODE SFEX",
                "Location Float(Power)",
                "Location",
                "world effect",
                "Create fire explosion without break blocks",
                new SimpleWorker(new int[]{0xA4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float power = v.pop(Float.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), power, false, false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SPAWNITEM",
                "SPAWNITEM SPI",
                "Location(A) ItemStack(B)",
                "Item(entity)",
                "world item",
                "Spawn specific item at location",
                new SimpleWorker(new int[]{0xA5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack i = v.pop(ItemStack.class);
                        Location l = v.pop(Location.class);
                        v.push(l.getWorld().dropItem(l, i));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DROPITEM",
                "DROPITEM DPI",
                "Location ItemStack",
                "Location",
                "world item",
                "Drop item (spawn with random offset)",
                new SimpleWorker(new int[]{0xA6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack itemst = v.pop(ItemStack.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().dropItemNaturally(l, itemst);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GENERATETREE",
                "GENERATETREE TREE",
                "Location #TreeType",
                "Location",
                "world effect",
                "Generate tree",
                new SimpleWorker(new int[]{0xA7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        TreeType t = v.pop(TreeType.class);
                        Location l = v.peek(Location.class);
                        l.getWorld().generateTree(l, t);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ALLOWANIMALS",
                "ALLOWANIMALS",
                "World",
                "Boolean",
                "world gamerules",
                "Returns true if animal's spawn allowed in this world",
                new SimpleWorker(new int[]{0xA8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getAllowAnimals());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ALLOWMONSTERS",
                "ALLOWMONSTERS",
                "World",
                "Boolean",
                "world gamerules",
                "Returns true if monster's spawn allowed in this world",
                new SimpleWorker(new int[]{0xA9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getAllowMonsters());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "AMBIENTLIMIT",
                "AMBIENTLIMIT",
                "World",
                "Integer",
                "world gamerules",
                "Gets the limit for number of ambient mobs that can spawn in a chunk in this world",
                new SimpleWorker(new int[]{0xAA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getAmbientSpawnLimit());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ANIMALSLIMIT",
                "ANIMALSLIMIT",
                "World",
                "Integer",
                "world gamerules",
                "Gets the limit for number of animals that can spawn in a chunk in this world",
                new SimpleWorker(new int[]{0xAB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getAnimalSpawnLimit());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DIFFICULTY",
                "DIFFICULTY",
                "World",
                "Difficulty",
                "world",
                "Get world difficulty",
                new SimpleWorker(new int[]{0xAD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getDifficulty());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GAMERULES",
                "GAMERULES GRS",
                "World",
                "ArrayList<String>",
                "world gamerules",
                "Get world gamerules",
                new SimpleWorker(new int[]{0xAE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(Arrays.asList(w.getGameRules()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GAMERULE",
                "GAMERULE GR",
                "World String(rule)",
                "String",
                "world gamerules",
                "Get value of gamerule in world",
                new SimpleWorker(new int[]{0xAF, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        World w = v.pop(World.class);
                        v.push(w.getGameRuleValue(s));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GENERATOR",
                "GENERATOR",
                "World",
                "ChunkGenerator",
                "world",
                "Get world generator",
                new SimpleWorker(new int[]{0xAF, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getGenerator());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TOPBLOCK",
                "TOPBLOCK TOP",
                "Block(position)",
                "Block(top)",
                "world",
                "Get highest block (exclude air)",
                new SimpleWorker(new int[]{0xAF, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getWorld().getHighestBlockAt(b.getX(), b.getZ()));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MAXHEIGHT",
                "MAXHEIGHT",
                "World",
                "Integer",
                "world",
                "Get max height of world",
                new SimpleWorker(new int[]{0xAF, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getMaxHeight());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MONSTERLIMIT",
                "MONSTERLIMIT",
                "World",
                "Integer",
                "world gamerules",
                "Gets limit for number of monsters that can spawn in a chunk in this world",
                new SimpleWorker(new int[]{0xAF, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getMonsterSpawnLimit());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORLDNAME",
                "WORLDNAME",
                "World",
                "String",
                "world",
                "Get world name",
                new SimpleWorker(new int[]{0xAF, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PVP",
                "PVP",
                "World",
                "Boolean",
                "world gamerules",
                "Gets the current PVP setting for this world",
                new SimpleWorker(new int[]{0xAF, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getPVP());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SEALVL",
                "SEALVL",
                "World",
                "Integer",
                "world",
                "Get sea level",
                new SimpleWorker(new int[]{0xAF, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getSeaLevel());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SEED",
                "SEED",
                "World",
                "Integer",
                "world",
                "Gets the Seed for this world",
                new SimpleWorker(new int[]{0xAF, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World l = v.pop(World.class);
                        v.push(l.getSeed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SPAWN",
                "SPAWN",
                "",
                "Location",
                "world",
                "Get spawn location of your world",
                new SimpleWorker(new int[]{0xAF, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram().getCaller().getLocation().getWorld().getSpawnLocation());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORLDSPAWN",
                "WORLDSPAWN WSPAWN",
                "World",
                "Location",
                "world",
                "Get spawn location of world",
                new SimpleWorker(new int[]{0xAF, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getSpawnLocation());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORLDTIME",
                "TIME WORLDTIME",
                "World",
                "Long",
                "world",
                "Get world time",
                new SimpleWorker(new int[]{0xAF, 0x0B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getTime());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WATERLIMIT",
                "WATERLIMIT",
                "World",
                "Integer",
                "world",
                "Gets the limit for number of water animals that can spawn in a chunk in this world",
                new SimpleWorker(new int[]{0xAF, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getWaterAnimalSpawnLimit());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WEATHERDURATION",
                "WEATHERDURATION WTHDUR",
                "World",
                "Integer",
                "world",
                "Get the remaining time in ticks of the current conditions",
                new SimpleWorker(new int[]{0xAF, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getWeatherDuration());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORLDTYPE",
                "WORLDTYPE WTYPE",
                "World",
                "WorldType",
                "world",
                "Gets the type of world",
                new SimpleWorker(new int[]{0xAF, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.getWorldType());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "STORM",
                "STORM",
                "World",
                "Boolean",
                "world",
                "Returns true if weather in world stormy",
                new SimpleWorker(new int[]{0xAF, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.hasStorm());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SUN",
                "SUN",
                "World",
                "Boolean",
                "world",
                "Returns true if weather in world sunny",
                new SimpleWorker(new int[]{0xAF, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(!(w.hasStorm()));

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "AUTOSAVE",
                "AUTOSAVE",
                "World",
                "Boolean",
                "world",
                "Gets whether or not the world will automatically save",
                new SimpleWorker(new int[]{0xAF, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        v.push(w.isAutoSave());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SAVE",
                "SAVE",
                "World",
                "World",
                "world",
                "Save world",
                new SimpleWorker(new int[]{0xAF, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.peek(World.class);
                        w.save();

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETAMBIENTLIMIT",
                "SETAMBIENTLIMIT SETAMBLMT >AMBLMT",
                "World Integer",
                "World",
                "world gamerules",
                "Sets spawn limit of ambients",
                new SimpleWorker(new int[]{0xAF, 0x14}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        World w = v.peek(World.class);
                        w.setAmbientSpawnLimit(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETANIMALSLIMIT",
                "SETANIMALSLIMIT SETANMLMT >ANMLMT",
                "World Integer",
                "World",
                "world gamerules",
                "Sets spawn limit of animals",
                new SimpleWorker(new int[]{0xAF, 0x15}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        World w = v.peek(World.class);
                        w.setAnimalSpawnLimit(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWATERLIMIT",
                "SETWATERLIMIT SETWTRLMT >WTRLMT",
                "World Integer",
                "World",
                "world gamerules",
                "Sets spawn limit of water mobs",
                new SimpleWorker(new int[]{0xAF, 0x16}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        World w = v.peek(World.class);
                        w.setWaterAnimalSpawnLimit(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETMONSTERSLIMIT",
                "SETMONSTERSLIMIT SETMONLIMIT >MONLMT",
                "World Integer",
                "World",
                "world gamerules",
                "Sets spawn limit of monsters",
                new SimpleWorker(new int[]{0xAF, 0x17}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        World w = v.peek(World.class);
                        w.setMonsterSpawnLimit(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETAUTOSAVE",
                "SETAUTOSAVE >AUTOSAVE",
                "World Boolean",
                "World",
                "world",
                "Set world autosave on/off",
                new SimpleWorker(new int[]{0xAF, 0x18}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        World w = v.peek(World.class);
                        w.setAutoSave(b);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETGAMERULE",
                "SETGAMERULE SETGR >GR",
                "World String(Gamerule) String(Value)",
                "World",
                "world gamerules",
                "Set gamerule value\nExample: ME \"doFireTicks\" \"false\" SETGR",
                new SimpleWorker(new int[]{0xAF, 0x19}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s1 = v.pop(String.class);
                        String s2 = v.pop(String.class);
                        World w = v.peek(World.class);
                        w.setGameRuleValue(s2, s1);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETDIFFICULTY",
                "SETDIFFICULTY SETDIF >DIF",
                "World Difficulty",
                "World",
                "world",
                "Set difficulty",
                new SimpleWorker(new int[]{0xAF, 0x1A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Difficulty g = v.pop(Difficulty.values());
                        World w = v.peek(World.class);
                        w.setDifficulty(g);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTIME",
                "SETTIME >TIME",
                "World Long",
                "World",
                "world",
                "Set time",
                new SimpleWorker(new int[]{0xAF, 0x1B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long s1 = v.pop(Long.class);
                        World w = v.peek(World.class);
                        w.setFullTime(s1);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPVP",
                "SETPVP >PVP",
                "World Boolean",
                "World",
                "world gamerules",
                "Set pvp status",
                new SimpleWorker(new int[]{0xAF, 0x1C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        World w = v.peek(World.class);
                        w.setPVP(b);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALLOWMONSTERS",
                "SETALLOWMONSTERS >ALLOWMONSTERS",
                "World Boolean",
                "World",
                "world gamerules",
                "Set allow monsters",
                new SimpleWorker(new int[]{0xAF, 0x1D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        World w = v.peek(World.class);
                        w.setSpawnFlags(b, w.getAllowAnimals());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALLOWANIMALS",
                "SETALLOWANIMALS >ALLOWANIMALS",
                "World Boolean",
                "World",
                "world gamerules",
                "Set allow animals",
                new SimpleWorker(new int[]{0xAF, 0x1E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        World w = v.peek(World.class);
                        w.setSpawnFlags(w.getAllowMonsters(), b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSPAWN",
                "SETSPAWN >SPAWN",
                "Location",
                "Location",
                "world",
                "Set spawn location of world",
                new SimpleWorker(new int[]{0xAF, 0x1F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.peek(Location.class);
                        l.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSTORM",
                "SETSTORM >STORM",
                "World",
                "World",
                "world",
                "Set world weather to storm",
                new SimpleWorker(new int[]{0xAF, 0x20}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.peek(World.class);
                        w.setStorm(true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSUN",
                "SETSUN >SUN",
                "World",
                "World",
                "world",
                "Set world weather to sunny",
                new SimpleWorker(new int[]{0xAF, 0x21}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.peek(World.class);
                        w.setStorm(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BOLT",
                "BOLT",
                "Location",
                "Location",
                "world",
                "Strikes lightning at the given Location",
                new SimpleWorker(new int[]{0xAF, 0x22}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.peek(Location.class);
                        l.getWorld().strikeLightning(l);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FAKEBOLT",
                "FAKEBOLT FBOLT",
                "Location",
                "Location",
                "world",
                "Strikes lightning at the given Location without doing damage",
                new SimpleWorker(new int[]{0xAF, 0x23}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.peek(Location.class);
                        l.getWorld().strikeLightningEffect(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CREATEWORLD",
                "CREATEWORLD",
                "WorldCreator",
                "World",
                "world",
                "Create new world with world creator",
                new SimpleWorker(new int[]{0xAF, 0x24}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                Bukkit.createWorld(
                                        v.pop(WorldCreator.class)
                                )
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORLDCREATOR",
                "WORLDCREATOR WCR",
                "String(worldname)",
                "WorldCreator",
                "world",
                "Create new default world creator",
                new SimpleWorker(new int[]{0xAF, 0x25}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                new WorldCreator(v.pop(String.class))
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETENVIRONMENT",
                "SETENVIRONMENT >ENV SETENV",
                "WorldCreator Environment",
                "WorldCreator",
                "world",
                "Set environment of worldcreator",
                new SimpleWorker(new int[]{0xAF, 0x26}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World.Environment environment = v.pop(World.Environment.values());
                        v.peek(WorldCreator.class).environment(environment);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSTRUCTURES",
                "SETSTRUCTURES >STRUCT SETSTRUCT",
                "WorldCreator Boolean",
                "WorldCreator",
                "world",
                "Set \"generate structures\" flag of worldcreator",
                new SimpleWorker(new int[]{0xAF, 0x27}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean val = v.pop(Boolean.class);
                        v.peek(WorldCreator.class).generateStructures(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETGENERATOR",
                "SETGENERATOR >GEN SETGEN",
                "WorldCreator Generator",
                "WorldCreator",
                "world",
                "Set generator of worldcreator",
                new SimpleWorker(new int[]{0xAF, 0x28}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ChunkGenerator val = v.pop(ChunkGenerator.class);
                        v.peek(WorldCreator.class).generator(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSEED",
                "SETSEED >SEED",
                "WorldCreator Long",
                "WorldCreator",
                "world",
                "Set seed of worldcreator",
                new SimpleWorker(new int[]{0xAF, 0x29}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long val = v.pop(Long.class);
                        v.peek(WorldCreator.class).seed(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWORLDTYPE",
                "SETWORLDTYPE >WTYPE SETWTYPE",
                "WorldCreator WorldType",
                "WorldCreator",
                "world",
                "Set type of worldcreator",
                new SimpleWorker(new int[]{0xAF, 0x2A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        WorldType val = v.pop(WorldType.values());
                        v.peek(WorldCreator.class).type(val);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "UNLOADWORLD",
                "UNLOADWORLD UNLOAD",
                "World",
                "",
                "world",
                "Unload world",
                new SimpleWorker(new int[]{0xAF, 0x2B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Bukkit.unloadWorld(
                                v.pop(World.class),
                                false
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CBOLT",
                "CBOLT",
                "Location",
                "Entity(LightingStrike)",
                "world",
                "Strikes lightning to the given Location",
                new SimpleWorker(new int[]{0xAF, 0x2C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(
                                l.getWorld().strikeLightning(l)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SOUND",
                "SOUND SND",
                "Location #Sound Float(volume) Float(pitch)",
                "Location",
                "world sound",
                "Play sound in location",
                new SimpleWorker(new int[]{0xAF, 0x2D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float pitch = v.pop(Float.class);
                        Float volume = v.pop(Float.class);
                        Sound s = v.pop(Sound.values());
                        Location l = v.peek(Location.class);
                        l.getWorld().playSound(l, s, volume, pitch);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYEFFECT",
                "PLAYEFFECT PLEF",
                "Location #Effect",
                "Location",
                "effect",
                "Play effect for all players",
                new SimpleWorker(new int[]{0xAF, 0x2E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Effect ef = v.pop(Effect.values());
                        Location l = v.pop(Location.class);
                        l.getWorld().playEffect(l, ef, 0);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYCEFFECT",
                "PLAYCEFFECT PLCEF",
                "Location #Effect Integer(data)",
                "Location",
                "effect",
                "Play custom effect for all players",
                new SimpleWorker(new int[]{0xAF, 0x2F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer data = v.pop(Integer.class);
                        Effect ef = v.pop(Effect.values());
                        Location l = v.peek(Location.class);
                        l.getWorld().playEffect(l, ef, (int) data);
                    }
                }
        ));

//        VSCompiler.addRule(new SimpleCompileRule(
//                "MAKEFIREWORK",
//                "MAKEFIREWORK MAKEFW",
//                "List(int_colors) List(int_fadecolors) Boolean(flicker)  Boolean(trail) #FireworkEffect.Type",
//                "Location",
//                "effect",
//                "Play custom effect for all players",
//                new SimpleWorker(new int[]{0xAF,0x30}){
//                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
//
//                        FireworkEffect.Type type = v.pop(FireworkEffect.Type.values());
//                        boolean trail = v.pop(Boolean.class);
//                        boolean flicker = v.pop(Boolean.class);
//                        List a_fade = v.pop(List.class);
//                        List a_colors = v.pop(List.class);
//
//                        List<Color> m_fade = new ArrayList<Color>();
//                        List<Color> m_colors = new ArrayList<Color>();
//                        for(Object color:a_colors){
//                            if(color instanceof Color) m_colors.add((Color)color);
//                            else m_colors.add(Color.fromRGB(v.convert(Integer.class,color)));
//                        }
//                        for(Object color:a_fade){
//                            if(color instanceof Color) m_fade.add((Color)color);
//                            else m_fade.add(Color.fromRGB(v.convert(Integer.class,color)));
//                        }
//
//                        ImmutableList<Color> colors = ImmutableList.copyOf(m_colors);
//                        ImmutableList<Color> fade = ImmutableList.copyOf(m_fade);
//                        FireworkEffect fireworkEffect = new FireworkEffect(
//                                flicker,
//                                trail,
//                                colors,
//                                fade,
//                                type
//                        );
//                        v.push(fireworkEffect);
//                    }
//                }
//        ));


    }


    public
    @EventHandler
    void onBreakPiston(BlockBreakEvent e) {
        if (e.getBlock().getTypeId() != 33) return;
        ItemStack hand = e.getPlayer().getItemInHand();
        if (hand.getTypeId() != 341) return;
        e.setCancelled(true);
        e.getBlock().setTypeId(29);
        int amt = hand.getAmount();
        if (amt > 1) hand.setAmount(amt - 1);
        else if (amt >= 0) e.getPlayer().setItemInHand(null);
    }
}
