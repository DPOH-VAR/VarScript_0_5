package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;
import org.bukkit.Location;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitLocation {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "LOCATIONX",
                "LOCATIONX LX",
                "Location",
                "Double",
                "location",
                "Get location x",
                new SimpleWorker(new int[]{0x60}) {
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
                new SimpleWorker(new int[]{0x61}) {
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
                new SimpleWorker(new int[]{0x62}) {
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
                new SimpleWorker(new int[]{0x63}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double x = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setZ(x);
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
                new SimpleWorker(new int[]{0x64}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double y = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setZ(y);
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
                new SimpleWorker(new int[]{0x65}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Location l = v.pop(Location.class);
                        l.setZ(z);
                        v.push(l);
                    }
                }
        ));


















    }


}
