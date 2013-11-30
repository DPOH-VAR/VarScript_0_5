package me.dpohvar.varscript.se;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;

import javax.script.ScriptEngine;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.07.13
 * Time: 22:49
 */
public class SECallerProgram extends SEProgram {

    public SECallerProgram(Runtime runtime, final Caller caller, ScriptEngine engine) {
        super(runtime, caller, engine);
    }

    public SECallerProgram(Runtime runtime, final Caller caller, ScriptEngine engine, Map<String, Object> scope) {
        super(runtime, caller, engine, scope);
    }

    public SECallerProgram(Runtime runtime, final Caller caller, ScriptEngine engine, Map<String, Object> scope, Map<String, Object> visible) {
        super(runtime, caller, engine, scope, visible);
    }
}