package org.dpohvar.varscript.vs.init;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.utils.region.CubeArea;
import org.dpohvar.varscript.utils.region.CubeRegion;
import org.dpohvar.varscript.utils.region.Region;
import org.dpohvar.varscript.utils.region.SphereRegion;
import org.dpohvar.varscript.vs.Context;
import org.dpohvar.varscript.vs.SimpleWorker;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.ThreadRunner;
import org.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitLocVec {
    private static Random random = new Random();

    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATION",
                "LOCATION",
                "Double(X) Double(Y) Double(Z) World",
                "Location",
                "location",
                "Create new location",
                new SimpleWorker(new int[]{0x60}) {
                    @Override
                    public void run(ThreadRunner r, org.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        Double z = v.pop(Double.class);
                        Double y = v.pop(Double.class);
                        Double x = v.pop(Double.class);
                        v.push(new Location(w, x, y, z));
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
                        v.push(l.getZ());
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
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                        v.push(new Vector(x, y, z));
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
                        vec.setY(y);
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
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        vec.setZ(z);
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
                new SimpleWorker(new int[]{0x6F, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(val, 0, 0);
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
                new SimpleWorker(new int[]{0x6F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(0, val, 0);
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
                new SimpleWorker(new int[]{0x6F, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.add(0, 0, val);
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
                new SimpleWorker(new int[]{0x6F, 0x03}) {
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
                new SimpleWorker(new int[]{0x6F, 0x04}) {
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
                new SimpleWorker(new int[]{0x6F, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location loc2 = v.pop(Location.class);
                        Location loc1 = v.pop(Location.class);
                        if (loc1 == null || loc2 == null || !loc1.getWorld().equals(loc2.getWorld())) {
                            v.push(null);
                        } else {
                            v.push(loc2.toVector().subtract(loc1.toVector()));
                        }
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORXADD",
                "VECTORXADD VXADD VX+",
                "Vector Double",
                "Vector",
                "vector",
                "Add value to vector x",
                new SimpleWorker(new int[]{0x6F, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        v.push(new Vector(vec.getX() + val, vec.getY(), vec.getZ()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORYADD",
                "VECTORYADD VYADD VY+",
                "Vector Double",
                "Vector",
                "vector",
                "Add value to vector y",
                new SimpleWorker(new int[]{0x6F, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        v.push(new Vector(vec.getX(), vec.getY() + val, vec.getZ()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORZADD",
                "VECTORZADD VZADD VZ+",
                "Vector Double",
                "Vector",
                "vector",
                "Add value to vector z",
                new SimpleWorker(new int[]{0x6F, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        v.push(new Vector(vec.getX(), vec.getY(), vec.getZ() + val));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORANGLE",
                "VECTORANGLE VANG",
                "Vector(A) Vector(B)",
                "Float",
                "vector",
                "get angle from vector A to vector B",
                new SimpleWorker(new int[]{0x6F, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec2 = v.pop(Vector.class);
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.angle(vec2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORMUL",
                "VECTORMUL V*",
                "Vector(A) Double",
                "Vector(Multiplied)",
                "vector",
                "Multiply vector to scalar",
                new SimpleWorker(new int[]{0x6F, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.multiply(val));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORLEN",
                "VECTORLEN VLEN",
                "Vector",
                "Double",
                "vector",
                "Get length of vector",
                new SimpleWorker(new int[]{0x6F, 0x0B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.length());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "NORMALIZE",
                "NORMALIZE NRM",
                "Vector(Source)",
                "Vector(Normalized)",
                "vector",
                "Normalize vector",
                new SimpleWorker(new int[]{0x6F, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.normalize());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VRANDOM",
                "VRANDOM VRND",
                "",
                "Vector",
                "vector",
                "Generate random vector with length 1",
                new SimpleWorker(new int[]{0x6F, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double ang1 = Math.PI * 2 * random.nextDouble();
                        double ang2 = Math.PI * random.nextDouble();
                        Vector l = new Vector(Math.sin(ang1), Math.cos(ang1), Math.cos(ang2));
                        v.push(l.normalize());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONPITCH",
                "LOCATIONPITCH LPT",
                "Location",
                "Float",
                "location",
                "Get location pitch",
                new SimpleWorker(new int[]{0x6F, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(l.getPitch());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONYAW",
                "LOCATIONYAW LYW",
                "Location",
                "Float",
                "location",
                "Get location yaw",
                new SimpleWorker(new int[]{0x6F, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        v.push(l.getYaw());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONYAW",
                "SETLOCATIONYAW SETLYW >LYW",
                "Location Float",
                "Location",
                "location",
                "Set location yaw",
                new SimpleWorker(new int[]{0x6F, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float val = v.pop(Float.class);
                        Location l = v.pop(Location.class);
                        l.setYaw(val);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLOCATIONPITCH",
                "SETLOCATIONPITCH SETLPT >LPT",
                "Location Float",
                "Location",
                "location",
                "Set location pitch",
                new SimpleWorker(new int[]{0x6F, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float val = v.pop(Float.class);
                        Location l = v.pop(Location.class);
                        l.setPitch(val);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONYAWADD",
                "LOCATIONYAWADD LYWADD LYW+",
                "Location Float",
                "Location",
                "location",
                "Add value to location yaw",
                new SimpleWorker(new int[]{0x6F, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float val = v.pop(Float.class);
                        Location l = v.pop(Location.class);
                        l.setYaw(l.getYaw() + val);
                        v.push(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONPITCHADD",
                "LOCATIONPITCHADD LPTADD LPT+",
                "Location Float",
                "Location",
                "location",
                "Add value to location pitch",
                new SimpleWorker(new int[]{0x6F, 0x13}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float val = v.pop(Float.class);
                        Location l = v.pop(Location.class);
                        l.setPitch(l.getPitch() + val);
                        v.push(l);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SPHERE",
                "SPHERE SPH",
                "Location Double(radius)",
                "Region",
                "region",
                "Create new sphere",
                new SimpleWorker(new int[]{0x6F, 0x14}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double radius = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        v.push(new SphereRegion(l, radius));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SPHEREAREA",
                "SPHEREAREA SPHA",
                "Location(center) Double(radius)",
                "Region",
                "region",
                "Create new sphere",
                new SimpleWorker(new int[]{0x6F, 0x15}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double radius = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        v.push(new SphereRegion(l, radius));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CUBE",
                "CUBE",
                "Location(center) Double(width/2)",
                "Region",
                "region",
                "Create new cube region",
                new SimpleWorker(new int[]{0x6F, 0x16}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double radius = v.pop(Double.class);
                        Location l1 = v.pop(Location.class);
                        Location l2 = l1.clone();
                        l1.add(radius, radius, radius);
                        l2.subtract(radius, radius, radius);
                        v.push(new CubeRegion(l1, l2));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BOX",
                "BOX",
                "Location(A) Location(B)",
                "Region",
                "region",
                "Create new box region from location A to location B",
                new SimpleWorker(new int[]{0x6F, 0x17}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l2 = v.pop(Location.class);
                        Location l1 = v.pop(Location.class);
                        v.push(new CubeRegion(l1, l2));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CUBEAREA",
                "CUBEAREA CUBEA",
                "Location(center) Double(width/2)",
                "Region",
                "region",
                "Create new cube area",
                new SimpleWorker(new int[]{0x6F, 0x18}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double radius = v.pop(Double.class);
                        Location l1 = v.pop(Location.class);
                        Location l2 = l1.clone();
                        l1.add(radius, 0, radius);
                        l2.subtract(radius, 0, radius);
                        v.push(new CubeArea(l1, l2));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BOXAREA",
                "BOXAREA CUBEABA",
                "Location(A) Location(B)",
                "Region",
                "region",
                "Create new cube area from location A to location B",
                new SimpleWorker(new int[]{0x6F, 0x19}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l2 = v.pop(Location.class);
                        Location l1 = v.pop(Location.class);
                        v.push(new CubeRegion(l1, l2));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGIONHAS",
                "REGIONHASR RHAS",
                "Region(A) Location(B)",
                "Boolean",
                "region",
                "True, if region A has location B",
                new SimpleWorker(new int[]{0x6F, 0x1A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        Region region = v.pop(Region.class);
                        v.push(region.hasLocation(l));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "INSIDE",
                "INSIDE INS",
                "Region(A) Collection(B)",
                "List",
                "region",
                "Get all objects from B that places inside region A",
                new SimpleWorker(new int[]{0x6F, 0x1B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Object> list = new ArrayList<Object>();
                        Collection col = v.pop(Collection.class);
                        Region region = v.pop(Region.class);
                        for (Object o : col) {
                            if (region.hasLocation(v.convert(Location.class, o))) {
                                list.add(o);
                            }
                        }
                        v.push(list);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SCAN",
                "SCAN",
                "Collection(A) Region(B)",
                "List",
                "region",
                "Get all objects from A that places inside region B",
                new SimpleWorker(new int[]{0x6F, 0x1C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Object> list = new ArrayList<Object>();
                        Region region = v.pop(Region.class);
                        Collection col = v.pop(Collection.class);
                        for (Object o : col) {
                            if (region.hasLocation(v.convert(Location.class, o))) {
                                list.add(o);
                            }
                        }
                        v.push(list);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKS",
                "BLOCKS",
                "Region",
                "Set(Block)",
                "region block",
                "Get all blocks in region",
                new SimpleWorker(new int[]{0x6F, 0x1D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Region region = v.pop(Region.class);
                        v.push(region.getBlocks());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "EDGE",
                "EDGE",
                "Region",
                "Set(Block)",
                "region block",
                "Get all edge blocks in region",
                new SimpleWorker(new int[]{0x6F, 0x1E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Region region = v.pop(Region.class);
                        v.push(region.getOutsideBlocks());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DIRECTION",
                "DIRECTION DIR",
                "Location",
                "Vector",
                "location vector",
                "get direction of location",
                new SimpleWorker(new int[]{0x6F, 0x1F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Location.class).getDirection()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SOLIDBLOCKS",
                "SOLIDBLOCKS SOLID",
                "Region",
                "Set(Block)",
                "region block",
                "Get all solid blocks in region",
                new SimpleWorker(new int[]{0x6F, 0x20}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Region region = v.pop(Region.class);
                        v.push(region.getSolidBlocks());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKSID",
                "BLOCKSID BSID",
                "Region Integer(id)",
                "Set(Block)",
                "region block",
                "Get blocks in region with selected id",
                new SimpleWorker(new int[]{0x6F, 0x21}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer id = v.pop(Integer.class);
                        Region region = v.pop(Region.class);
                        v.push(region.getBlocks(id));
                    }
                }
        ));


    }
}
