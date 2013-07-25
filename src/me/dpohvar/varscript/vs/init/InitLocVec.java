package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitLocVec {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATION",
                "LOCATION",
                "Double(X) Double(Y) Double(Z) World",
                "Location",
                "location",
                "Create new location",
                new SimpleWorker(new int[]{0x60}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        Double z = v.pop(Double.class);
                        Double y = v.pop(Double.class);
                        Double x = v.pop(Double.class);
                        v.push(new Location(w,x,y,z));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONX",
                "LOCATIONX LX",
                "Location",
                "Double",
                "location",
                "Get location x",
                new SimpleWorker(new int[]{0x61}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(l.getX());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONY",
                "LOCATIONY LY",
                "Location",
                "Double",
                "location",
                "Get location y",
                new SimpleWorker(new int[]{0x62}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(l.getY());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONZ",
                "LOCATIONZ LZ",
                "Location",
                "Double",
                "location",
                "Get location z",
                new SimpleWorker(new int[]{0x63}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(l.getY());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONX",
                "SETLOCATIONX SETLX >LX",
                "Location Double",
                "Location",
                "location",
                "Set location x",
                new SimpleWorker(new int[]{0x64}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double x = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setX(x);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONY",
                "SETLOCATIONY SETLY >LY",
                "Location Double",
                "Location",
                "location",
                "Set location y",
                new SimpleWorker(new int[]{0x65}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double y = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setY(y);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONZ",
                "SETLOCATIONZ SETLZ >LZ",
                "Location Double",
                "Location",
                "location",
                "Set location z",
                new SimpleWorker(new int[]{0x66}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setZ(z);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONWORLD",
                "LSETOCATIONWORLD SETLW >LW",
                "Location World",
                "Location",
                "location world",
                "Change world of location",
                new SimpleWorker(new int[]{0x67}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        Location loc = v.pop(Location.class);
                        loc.setWorld(w);
                        v.push(loc);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "VECTOR",
                "VECTOR",
                "Double(X) Double(Y) Double(Z)",
                "Vector",
                "vector",
                "Create new vector",
                new SimpleWorker(new int[]{0x68}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Double y = v.pop(Double.class);
                        Double x = v.pop(Double.class);
                        v.push(new Vector(x,y,z));
                    }
                }
        ));



        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORX",
                "VECTORX VX",
                "Vector",
                "Double",
                "vector",
                "Get vector x",
                new SimpleWorker(new int[]{0x69}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.getX());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORY",
                "VECTORY VY",
                "Vector",
                "Double",
                "vector",
                "Get vector y",
                new SimpleWorker(new int[]{0x6A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.getY());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORZ",
                "VECTORZ VZ",
                "Vector",
                "Double",
                "vector",
                "Get vector z",
                new SimpleWorker(new int[]{0x6B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.getZ());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETVECTORX",
                "SETVECTORX SETVX >VX",
                "Vector Double",
                "Double",
                "vector",
                "Set vector x",
                new SimpleWorker(new int[]{0x6C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double x = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        vec.setX(x);
                        v.push(vec);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETVECTORY",
                "SETVECTORY SETVY >VY",
                "Vector Double",
                "Double",
                "vector",
                "Set vector y",
                new SimpleWorker(new int[]{0x6D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double y = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        vec.setX(y);
                        v.push(vec);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETVECTORZ",
                "SETVECTORZ SETVZ >VZ",
                "Vector Double",
                "Double",
                "vector",
                "Set vector z",
                new SimpleWorker(new int[]{0x6E}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        vec.setX(z);
                        v.push(vec);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONXADD",
                "LOCATIONXADD LXADD LX+",
                "Location Double",
                "Location",
                "location",
                "Add value to location x",
                new SimpleWorker(new int[]{0x6F,0x00}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(val,0,0);
                        v.push(l);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONYADD",
                "LOCATIONYADD LYADD LY+",
                "Location Double",
                "Location",
                "location",
                "Add value to location y",
                new SimpleWorker(new int[]{0x6F,0x01}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(0,val,0);
                        v.push(l);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONZADD",
                "LOCATIONZADD LZADD LZ+",
                "Location Double",
                "Location",
                "location",
                "Add value to location z",
                new SimpleWorker(new int[]{0x6F,0x02}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(0,0,val);
                        v.push(l);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONVADD",
                "LOCATIONVADD LV+ LVADD",
                "Location Vector",
                "Location",
                "vector location",
                "Add vector to location",
                new SimpleWorker(new int[]{0x6F,0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        Location loc = v.pop(Location.class);
                        loc.add(vec);
                        v.push(loc);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DISTANCE",
                "DISTANCE DIST",
                "Location Location",
                "Double",
                "location",
                "Get distance between locations",
                new SimpleWorker(new int[]{0x6F,0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location loc2 = v.pop(Location.class);
                        Location loc1 = v.pop(Location.class);
                        v.push(loc1.distance(loc2));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BETWEEN",
                "BETWEEN BTW",
                "Location(A) Location(B)",
                "Vector",
                "location vector",
                "Get vector from location A to location B",
                new SimpleWorker(new int[]{0x6F,0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location loc2 = v.pop(Location.class);
                        Location loc1 = v.pop(Location.class);
                        if(loc1==null||loc2==null||!loc1.getWorld().equals(loc2.getWorld())){
                            v.push(null);
                        } else {
                            v.push(loc1.toVector().multiply(-1).add(loc2.toVector()));
                        }
                    }
                }
        ));


















    }


}
