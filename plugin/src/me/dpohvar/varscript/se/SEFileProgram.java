package me.dpohvar.varscript.se;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;

import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.07.13
 * Time: 22:49
 */
public class SEFileProgram extends SEProgram {

    public SEFileProgram(Runtime runtime, Caller caller, ScriptEngine engine, String[] args) {
        super(runtime, caller, engine, newScope(args));
    }

    private static Map<String, Object> newScope(String[] args) {
        HashMap<String, Object> scope = new HashMap<String, Object>();
        scope.put("args", args);
        return scope;
    }
}