package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.se.SECallerProgram;
import me.dpohvar.varscript.se.SEProgram;

import javax.script.ScriptEngine;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.10.13
 * Time: 3:15
 */
public class Module {

    public final String name;
    public boolean loaded = false;
    protected Object langModule = null;
    protected SEProgram program;

    public Module(String name) {
        this.name = name;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Object load(Runtime runtime, ScriptEngine scriptEngine, String src) {
        String lang = scriptEngine.getFactory().getLanguageName();
        VarScript.instance.getLogger().info("[" + lang + "] --> loading module " + name);
        Caller caller = Caller.getCallerFor(this);
        this.program = new SECallerProgram(runtime, caller, scriptEngine, null);
        this.langModule = program.runScript(src);
        this.loaded = true;
        if (langModule == null) throw new RuntimeException("module returns null");
        VarScript.instance.getLogger().info("[" + lang + "] <#> module " + name + " loaded.");
        return langModule;
    }

    public void stop() {
        if (this.program != null) program.stop();
    }

    public Object getLangModule() {
        return langModule;
    }
}
