package me.dpohvar.varscript.scheduler.condition;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskCondition;
import me.dpohvar.varscript.se.SECallerProgram;

import javax.script.ScriptEngine;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class JSCondition extends TaskCondition {

    final String param;
    private ScriptEngine engine;
    private me.dpohvar.varscript.Runtime runtime;

    public JSCondition(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public boolean check(Map<String, Object> environment) {
        Caller caller = Caller.getCallerFor(getTask());
        SECallerProgram program = new SECallerProgram(runtime, caller, engine);
        for (Map.Entry<String, Object> e : environment.entrySet()) {
            program.putToEnvironment(e.getKey(), e.getValue());
        }
        try {
            Object result = program.runScript(param);
            if (result == null || result.equals(false)) return false;
            return true;
        } catch (Exception e) {
            caller.handleException(e);
            return false;
        }
    }

    @Override
    protected boolean register() {
        runtime = task.getScheduler().runtime;
        engine = runtime.getScriptEngine("JavaScript");
        if (param == null || engine == null || param.isEmpty()) return false;
        return true;
    }

    @Override
    protected boolean unregister() {
        if (engine == null) return false;
        engine = null;
        return true;
    }

    public static String getType() {
        return "JS";
    }

    @Override
    public String toString() {
        return "JS" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
