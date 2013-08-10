package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitSystem {
    public static void load(){

        VSCompiler.addRule(new SimpleCompileRule(
                "SYSTIME",
                "SYSTIME STIME",
                "",
                "Long",
                "system time",
                "current value of the system timer, in milliseconds",
                new SimpleWorker(new int[]{0x0F,0x80}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) {
                        v.push(System.currentTimeMillis());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NOW",
                "NOW",
                "",
                "Date",
                "system date",
                "get current system date",
                new SimpleWorker(new int[]{0x0F,0x81}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) {
                        v.push(new Date());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NANOTIME",
                "NANOTIME NTIME",
                "",
                "Long",
                "system time",
                "current value of the system timer, in nanoseconds",
                new SimpleWorker(new int[]{0x0F,0x82}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) {
                        v.push(System.nanoTime());
                    }
                }
        ));


    }
}
