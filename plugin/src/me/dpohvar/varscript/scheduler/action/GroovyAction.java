package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;
import me.dpohvar.varscript.se.SECallerProgram;

import javax.script.ScriptEngine;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class GroovyAction extends TaskAction {

    final String param;
    ScriptEngine engine;
    private me.dpohvar.varscript.Runtime runtime;

    public GroovyAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        if (engine == null) return;
        Caller caller = Caller.getCallerFor(getTask());
        SECallerProgram program = new SECallerProgram(runtime, caller, engine, null);
        for (Map.Entry<String, Object> e : environment.entrySet()) {
            program.putToEnvironment(e.getKey(), e.getValue());
        }
        try {
            program.runScript(param);
        } catch (Exception e) {
            caller.handleException(e);
        }
    }

    @Override
    protected boolean register() {
        runtime = task.getScheduler().runtime;
        engine = runtime.getEngine("groovy");
        if (param == null || param.isEmpty() || engine == null) return false;
        return true;
    }

    @Override
    protected boolean unregister() {
        if (engine == null) return false;
        engine = null;
        return true;
    }

    public static String getType() {
        return "G";
    }

    @Override
    public String toString() {
        return "G" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
