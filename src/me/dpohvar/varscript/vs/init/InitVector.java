package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitVector {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "VECTORX",
                "VECTORX VX",
                "Vector",
                "Double",
                "vector",
                "Get vector x",
                new SimpleWorker(new int[]{0x66}) {
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
                new SimpleWorker(new int[]{0x67}) {
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
                new SimpleWorker(new int[]{0x68}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        v.push(vec.getZ());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETVECTORX",
                "SETVECTORX SETVECX >VECX",
                "Vector Double",
                "Double",
                "vector",
                "Set vector x",
                new SimpleWorker(new int[]{0x69}) {
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
                "SETVECTORY SETVECY >VECY",
                "Vector Double",
                "Double",
                "vector",
                "Set vector y",
                new SimpleWorker(new int[]{0x6A}) {
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
                "SETVECTORZ SETVECZ >VECZ",
                "Vector Double",
                "Double",
                "vector",
                "Set vector z",
                new SimpleWorker(new int[]{0x6B}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Double z = v.pop(Double.class);
                        Vector vec = v.pop(Vector.class);
                        vec.setX(z);
                        v.push(vec);
                    }
                }
        ));



















    }


}
