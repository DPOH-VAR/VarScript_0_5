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
public class ScriptAction extends TaskAction {

    final String param;
    ScriptEngine engine;
    private me.dpohvar.varscript.Runtime runtime;
    private String script;

    public ScriptAction(Task task, String param) {
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
            program.runScript(script);
        } catch (Exception e) {
            caller.handleException(e);
        }
    }

    @Override
    protected boolean register() {
        runtime = task.getScheduler().runtime;
        if (param == null || param.isEmpty() || !param.contains(" ")) return false;
        int pos = param.indexOf(' ');
        String lang = param.substring(0, pos);
        String script = param.substring(pos + 1, param.length());
        engine = runtime.getEngine(lang);
        if (engine == null) return false;
        this.script = script;
        return true;
    }

    @Override
    protected boolean unregister() {
        if (engine == null) return false;
        engine = null;
        return true;
    }

    public static String getType() {
        return "SCRIPT";
    }

    @Override
    public String toString() {
        return "SCRIPT" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
