package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.permissions.Permissible;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitSystem {
    public static void load() {

        VSCompiler.addRule(new SimpleCompileRule(
                "SYSTIME",
                "SYSTIME STIME",
                "",
                "Long",
                "system time",
                "current value of the system timer, in milliseconds",
                new SimpleWorker(new int[]{0x0F, 0x80}) {
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
                new SimpleWorker(new int[]{0x0F, 0x81}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) {
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
                new SimpleWorker(new int[]{0x0F, 0x82}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        v.push(System.nanoTime());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASPERM",
                "HASPERM",
                "Permissible String(permission)",
                "Boolean(result)",
                "permission",
                "check permission of permissible object",
                new SimpleWorker(new int[]{0x0F, 0x83}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
                        String val = v.pop(String.class);
                        v.push(
                                v.pop(Permissible.class).hasPermission(val)
                        );
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "ISPERM",
                "ISPERM",
                "Permissible String(permission)",
                "Boolean(result)",
                "permission",
                "checks if permissible object contains an override for the specified permission, by fully qualified name",
                new SimpleWorker(new int[]{0x0F, 0x84}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
                        String val = v.pop(String.class);
                        v.push(
                                v.pop(Permissible.class).isPermissionSet(val)
                        );
                    }
                }
        ));


    }
}
