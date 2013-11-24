package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.Function;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.VarscriptProgram;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import javax.script.ScriptEngine;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.10.13
 * Time: 3:15
 */
public class VSModule extends Module {

    public VSModule(String name) {
        super(name);
    }

    public Object load(Runtime runtime, ScriptEngine scriptEngine, String src) {

        VarScript.instance.getLogger().info("[vs] --> loading module " + name);

        Caller caller = Caller.getCallerFor(this);
        try {
            VarscriptProgram program = new VarscriptProgram(runtime, caller, new HashMap<String, Object>());
            Thread thread = new Thread(program);
            Function function = VSCompiler.compile(src, name).build(program.getScope());
            langModule = thread.pushFunction(function);
            new ThreadRunner(thread).runThreads();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.loaded = true;
        VarScript.instance.getLogger().info("[vs] <#> module " + name + " loaded.");
        return langModule;
    }

    public void stop() {
        if (this.program != null) program.stop();
    }

    public Object getLangModule() {
        return langModule;
    }
}
